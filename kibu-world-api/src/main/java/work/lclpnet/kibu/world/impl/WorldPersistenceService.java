package work.lclpnet.kibu.world.impl;

import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.*;
import net.minecraft.registry.*;
import net.minecraft.resource.DataConfiguration;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.WorldSavePath;
import net.minecraft.world.GameRules;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionOptionsRegistryHolder;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.LevelInfo;
import net.minecraft.world.level.LevelProperties;
import net.minecraft.world.level.WorldGenSettings;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.world.level.storage.SaveVersionInfo;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import work.lclpnet.kibu.world.GameRuleAccess;
import work.lclpnet.kibu.world.mixin.LevelStorageAccessor;
import work.lclpnet.kibu.world.mixin.MinecraftServerAccessor;
import xyz.nucleoid.fantasy.Fantasy;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;
import xyz.nucleoid.fantasy.RuntimeWorldHandle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

@ApiStatus.Internal
public class WorldPersistenceService {

    private final MinecraftServer server;
    private final Logger logger;

    public WorldPersistenceService(MinecraftServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    public Optional<RuntimeWorldHandle> tryRecreateWorld(Identifier identifier) {
        RegistryKey<World> registryKey = RegistryKey.of(RegistryKeys.WORLD, identifier);

        Fantasy fantasy = Fantasy.get(server);
        ServerWorld world = server.getWorld(registryKey);

        if (world != null) {
            // world exists, it is safe to call getOrOpenPersistentWorld()
            RuntimeWorldHandle handle = fantasy.getOrOpenPersistentWorld(identifier, null);
            return Optional.of(handle);
        }

        // try to restore ChunkGenerator and DimensionType
        NbtCompound levelData = readLevelData(registryKey);

        if (levelData == null) {
            return Optional.empty();
        }

        RuntimeWorldConfig config;

        try {
            config = restoreConfig(levelData);
        } catch (Throwable t) {
            logger.error("Failed to restore runtime world config for world {}", identifier, t);
            return Optional.empty();
        }

        // config restored successfully
        RuntimeWorldHandle handle = fantasy.getOrOpenPersistentWorld(identifier, config);

        return Optional.of(handle);
    }

    @Nullable
    private RuntimeWorldConfig restoreConfig(NbtCompound levelData) {
        var levelPropertiesPair = getLevelPropertiesPair(levelData);

        SaveProperties properties = levelPropertiesPair.getFirst();
        DimensionOptionsRegistryHolder.DimensionsConfig dimensionsConfig = levelPropertiesPair.getSecond();

        DimensionOptions dimension = findMainDimension(dimensionsConfig);

        if (dimension == null) {
            logger.error("Could not find main dimension for level {}", properties.getLevelName());
            return null;
        }

        RuntimeWorldConfig config = new RuntimeWorldConfig()
                .setDimensionType(dimension.dimensionTypeEntry())
                .setGenerator(dimension.chunkGenerator())
                .setFlat(properties.isFlatWorld())
                .setDifficulty(properties.getDifficulty());

        if (properties instanceof LevelProperties levelProps) {
            GeneratorOptions generatorOptions = levelProps.getGeneratorOptions();
            config.setSeed(generatorOptions.getSeed());

            config.setRaining(levelProps.getRainTime());
            config.setRaining(levelProps.isRaining());
            config.setSunny(levelProps.getClearWeatherTime());
            config.setThundering(levelProps.isThundering());
            config.setThundering(levelProps.getThunderTime());
            config.setTimeOfDay(levelProps.getTimeOfDay());

            GameRules gameRules = levelProps.getGameRules();
            Map<GameRules.Key<?>, GameRules.Rule<?>> ruleMap = ((GameRuleAccess) gameRules).kibu$getRules();

            ruleMap.forEach((key, rule) -> {
                if (rule instanceof GameRules.BooleanRule booleanRule) {
                    //noinspection unchecked
                    config.setGameRule((GameRules.Key<GameRules.BooleanRule>) key, booleanRule.get());
                } else if (rule instanceof GameRules.IntRule intRule) {
                    //noinspection unchecked
                    config.setGameRule((GameRules.Key<GameRules.IntRule>) key, intRule.get());
                }
            });
        }

        return config;
    }

    @Nullable
    private DimensionOptions findMainDimension(DimensionOptionsRegistryHolder.DimensionsConfig config) {
        Registry<DimensionOptions> dimensions = config.dimensions();

        if (dimensions.contains(DimensionOptions.OVERWORLD)) {
            DimensionOptions overworld = dimensions.get(DimensionOptions.OVERWORLD);

            if (overworld != null) {
                return overworld;
            }
        }

        // there is no overworld entry, accept any other dimension
        var iterator = dimensions.iterator();

        if (iterator.hasNext()) {
            return iterator.next();
        }

        return null;
    }

    private Pair<SaveProperties, DimensionOptionsRegistryHolder.DimensionsConfig> getLevelPropertiesPair(NbtCompound levelData) {
        var registryManager = server.getRegistryManager();

        Lifecycle registryLifecycle = registryManager.getRegistryLifecycle();
        DataFixer dataFixer = server.getDataFixer();

        // from net.minecraft.world.level.storage.LevelStorage#createLevelDataParser
        NbtCompound data = levelData.getCompound("Data");

        NbtCompound player = data.contains("Player", NbtElement.COMPOUND_TYPE) ? data.getCompound("Player") : null;
        data.remove("Player");

        int dataVersion = NbtHelper.getDataVersion(data, -1);

        var ops = RegistryOps.of(NbtOps.INSTANCE, server.getRegistryManager());
        var dynamic = DataFixTypes.LEVEL.update(dataFixer, new Dynamic<>(ops, data), dataVersion);

        WorldGenSettings worldGenSettings = LevelStorageAccessor.invokeReadGeneratorProperties(dynamic, dataFixer, dataVersion)
                .getOrThrow(false, Util.addPrefix("WorldGenSettings: ", logger::error));

        SaveVersionInfo saveVersionInfo = SaveVersionInfo.fromDynamic(dynamic);

        DataConfiguration dataConfiguration = getDataConfiguration(data, server.getDataFixer());
        LevelInfo levelInfo = LevelInfo.fromDynamic(dynamic, dataConfiguration);

        // use an empty registry to only read the entries from the nbt
        Registry<DimensionOptions> existingDimOptions = new SimpleRegistry<>(RegistryKeys.DIMENSION, registryLifecycle);

        var dimensionsConfig = worldGenSettings.dimensionOptionsRegistryHolder()
                .toConfig(existingDimOptions);

        Lifecycle propsLifecycle = dimensionsConfig.getLifecycle().add(registryLifecycle);

        LevelProperties levelProperties = LevelProperties.readProperties(dynamic, dataFixer, dataVersion, player,
                levelInfo, saveVersionInfo, dimensionsConfig.specialWorldProperty(),
                worldGenSettings.generatorOptions(), propsLifecycle);

        return Pair.of(levelProperties, dimensionsConfig);
    }

    @NotNull
    private DataConfiguration getDataConfiguration(NbtCompound data, DataFixer dataFixer) {
        int dataVersion = NbtHelper.getDataVersion(data, -1);

        Dynamic<NbtElement> dataDynamic = new Dynamic<>(NbtOps.INSTANCE, data);
        Dynamic<NbtElement> dynamic = DataFixTypes.LEVEL.update(dataFixer, dataDynamic, dataVersion);

        return DataConfiguration.CODEC.parse(dynamic)
                .resultOrPartial(logger::error)
                .orElse(DataConfiguration.SAFE_MODE);
    }

    @Nullable
    private NbtCompound readLevelData(RegistryKey<World> registryKey) {
        Path directory = getWorldDirectory(registryKey);
        Path levelDat = directory.resolve(WorldSavePath.LEVEL_DAT.getRelativePath());

        if (!Files.exists(levelDat)) {
            logger.warn("Level data file does not exist at {}", levelDat);
            return null;
        }

        try (var in = Files.newInputStream(levelDat)) {
            return NbtIo.readCompressed(in);
        } catch (IOException e) {
            logger.error("Failed to read compressed nbt from {}", levelDat, e);
            return null;
        }
    }

    @NotNull
    private Path getWorldDirectory(RegistryKey<World> registryKey) {
        LevelStorage.Session session = ((MinecraftServerAccessor) server).getSession();

        return session.getWorldDirectory(registryKey);
    }
}