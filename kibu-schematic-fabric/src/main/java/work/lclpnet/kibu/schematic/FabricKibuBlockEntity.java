package work.lclpnet.kibu.schematic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.mc.KibuBlockEntity;
import work.lclpnet.kibu.mc.KibuBlockPos;
import work.lclpnet.kibu.nbt.FabricNbtConversion;

import java.util.Objects;

public class FabricKibuBlockEntity implements KibuBlockEntity {

    private static final Logger logger = LoggerFactory.getLogger(FabricKibuBlockEntity.class);

    private final BlockEntityType<?> type;
    private final BlockPos pos;
    private final net.minecraft.nbt.CompoundTag nbt;

    public FabricKibuBlockEntity(BlockEntity blockEntity) {
        this(blockEntity.getType(), blockEntity.getBlockPos(), blockEntity.saveWithoutMetadata(Objects.requireNonNull(blockEntity.getLevel()).registryAccess()));
    }

    public FabricKibuBlockEntity(BlockEntityType<?> type, BlockPos pos, net.minecraft.nbt.CompoundTag nbt) {
        this.type = type;
        this.pos = pos;
        this.nbt = nbt;
    }

    @Override
    public String getId() {
        ResourceLocation id = BlockEntityType.getKey(type);

        if (id == null) throw new IllegalStateException("Block entity type not registered");

        return id.toString();
    }

    @Override
    public KibuBlockPos getPosition() {
        return new KibuBlockPos(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public CompoundTag createNbt() {
        return FabricNbtConversion.convert(nbt, CompoundTag.class);
    }

    public boolean spawn(ServerLevel world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (!type.isValid(state)) return false;

        var optBlockEntity = world.getBlockEntity(pos, type);

        if (optBlockEntity.isPresent()) {
            readBlockEntityNbt(optBlockEntity.get(), world.registryAccess());
            return true;
        }

        BlockEntity blockEntity = type.create(pos, state);

        if (blockEntity == null) return false;

        readBlockEntityNbt(blockEntity, world.registryAccess());
        blockEntity.setLevel(world);

        world.setBlockEntity(blockEntity);

        return true;
    }

    private void readBlockEntityNbt(BlockEntity blockEntity, HolderLookup.Provider registries) {
        try (var logging = new ProblemReporter.ScopedCollector(blockEntity.problemPath(), FabricKibuBlockEntity.logger)) {
            blockEntity.loadWithComponents(TagValueInput.create(logging, registries, nbt));
        } catch (Throwable t) {
            FabricKibuBlockEntity.logger.error("Failed to read nbt data for block entity {} at {}", type, pos, t);
        }
    }
}
