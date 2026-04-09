package work.lclpnet.kibu.schematic.vanilla;

import com.mojang.datafixers.DataFixer;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.Block;
import work.lclpnet.kibu.schematic.api.*;
import work.lclpnet.kibu.schematic.type.KibuServerView;

public class VanillaStructureFormat implements SchematicFormat {

    private final DataFixer fixerUpper;
    private final HolderGetter<Block> blockLookup;
    private final HolderLookup.Provider registries;
    private volatile SchematicDeserializer deserializer = null;
    private volatile SchematicReader reader = null;

    public VanillaStructureFormat(DataFixer fixerUpper, HolderGetter<Block> blockLookup, HolderLookup.Provider registries) {
        this.fixerUpper = fixerUpper;
        this.blockLookup = blockLookup;
        this.registries = registries;
    }

    @Override
    public SchematicSerializer serializer() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public SchematicDeserializer deserializer() {
        if (deserializer != null) return deserializer;

        synchronized (this) {
            if (deserializer == null) {
                deserializer = new Deserializer(fixerUpper, blockLookup, registries);
            }
        }

        return deserializer;
    }

    @Override
    public SchematicWriter writer() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public SchematicReader reader() {
        if (reader != null) return reader;

        synchronized (this) {
            if (reader == null) {
                reader = new Reader(deserializer());
            }
        }

        return reader;
    }

    public static VanillaStructureFormat get(MinecraftServer server) {
        return ((KibuServerView) server).kibu$getVanillaStructureFormat();
    }
}
