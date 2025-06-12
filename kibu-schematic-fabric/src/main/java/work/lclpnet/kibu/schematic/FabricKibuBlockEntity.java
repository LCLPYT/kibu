package work.lclpnet.kibu.schematic;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.NbtReadView;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
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
    private final NbtCompound nbt;

    public FabricKibuBlockEntity(BlockEntity blockEntity) {
        this(blockEntity.getType(), blockEntity.getPos(), blockEntity.createNbt(Objects.requireNonNull(blockEntity.getWorld()).getRegistryManager()));
    }

    public FabricKibuBlockEntity(BlockEntityType<?> type, BlockPos pos, NbtCompound nbt) {
        this.type = type;
        this.pos = pos;
        this.nbt = nbt;
    }

    @Override
    public String getId() {
        Identifier id = BlockEntityType.getId(type);

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

    public boolean spawn(ServerWorld world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (!type.supports(state)) return false;

        var optBlockEntity = world.getBlockEntity(pos, type);

        if (optBlockEntity.isPresent()) {
            readBlockEntityNbt(optBlockEntity.get(), world.getRegistryManager());
            return true;
        }

        BlockEntity blockEntity = type.instantiate(pos, state);

        if (blockEntity == null) return false;

        readBlockEntityNbt(blockEntity, world.getRegistryManager());
        blockEntity.setWorld(world);

        world.addBlockEntity(blockEntity);

        return true;
    }

    private void readBlockEntityNbt(BlockEntity blockEntity, RegistryWrapper.WrapperLookup registries) {
        try (var logging = new ErrorReporter.Logging(blockEntity.getReporterContext(), FabricKibuBlockEntity.logger)) {
            blockEntity.read(NbtReadView.create(logging, registries, nbt));
        } catch (Throwable t) {
            FabricKibuBlockEntity.logger.error("Failed to read nbt data for block entity {} at {}", type, pos, t);
        }
    }
}
