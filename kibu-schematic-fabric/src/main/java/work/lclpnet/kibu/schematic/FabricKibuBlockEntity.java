package work.lclpnet.kibu.schematic;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.mc.KibuBlockEntity;
import work.lclpnet.kibu.mc.KibuBlockPos;
import work.lclpnet.kibu.nbt.FabricNbtConversion;

public class FabricKibuBlockEntity implements KibuBlockEntity {

    private final BlockEntityType<?> type;
    private final BlockPos pos;
    private final NbtCompound nbt;

    public FabricKibuBlockEntity(BlockEntity blockEntity) {
        this(blockEntity.getType(), blockEntity.getPos(), blockEntity.createNbt());
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
            optBlockEntity.get().readNbt(nbt);
            return true;
        }

        BlockEntity blockEntity = type.instantiate(pos, state);

        if (blockEntity == null) return false;

        blockEntity.readNbt(nbt);
        blockEntity.setWorld(world);

        world.addBlockEntity(blockEntity);

        return true;
    }
}
