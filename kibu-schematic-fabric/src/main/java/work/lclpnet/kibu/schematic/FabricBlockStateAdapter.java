package work.lclpnet.kibu.schematic;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import work.lclpnet.kibu.mc.*;
import work.lclpnet.kibu.nbt.FabricNbtConversion;
import work.lclpnet.kibu.util.BlockStateUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FabricBlockStateAdapter implements BlockStateAdapter {

    private final Map<BlockState, KibuBlockState> states = new HashMap<>();

    protected FabricBlockStateAdapter() {
    }

    public static FabricBlockStateAdapter getInstance() {
        return InstanceHolder.instance;
    }

    @Nullable
    @Override
    public KibuBlockState getBlockState(String string) {
        var nativeBlockState = getNativeBlockState(string);
        return nativeBlockState != null ? adapt(nativeBlockState) : null;
    }

    @Nullable
    public BlockState getNativeBlockState(String string) {
        return BlockStateUtils.parse(string);
    }

    @Nullable
    public BlockState revert(KibuBlockState state) {
        if (state instanceof FabricKibuBlockState fState) {
            return fState.getState();
        }

        // fallback to parsing the string representation (slow)
        return getNativeBlockState(state.getAsString());
    }

    public BlockPos revert(KibuBlockPos pos) {
        return new BlockPos(pos.getX(), pos.getY(), pos.getZ());
    }

    public KibuBlockState adapt(BlockState state) {
        return states.computeIfAbsent(state, FabricKibuBlockState::new);
    }

    public KibuBlockPos adapt(Vec3i pos) {
        return new KibuBlockPos(pos.getX(), pos.getY(), pos.getZ());
    }

    public KibuBlockEntity adapt(BlockEntity blockEntity) {
        return new FabricKibuBlockEntity(blockEntity);
    }

    public Optional<FabricKibuBlockEntity> revert(KibuBlockEntity blockEntity) {
        Identifier id = Identifier.tryParse(blockEntity.getId());

        return Registries.BLOCK_ENTITY_TYPE.getOrEmpty(id).map(type -> {
            BlockPos pos = revert(blockEntity.getPosition());
            NbtCompound nbt = FabricNbtConversion.convert(blockEntity.createNbt(), NbtCompound.class);

            return new FabricKibuBlockEntity(type, pos, nbt);
        });
    }

    public KibuEntity adapt(Entity entity) {
        return new FabricKibuEntity(entity);
    }

    public Optional<FabricKibuEntity> revert(KibuEntity entity) {
        return EntityType.get(entity.getId()).map(type -> {
            Vec3d pos = new Vec3d(entity.getX(), entity.getY(), entity.getZ());
            NbtCompound nbt = FabricNbtConversion.convert(entity.getExtraNbt(), NbtCompound.class);

            return new FabricKibuEntity(type, pos, nbt);
        });
    }

    private static final class InstanceHolder {
        private static final FabricBlockStateAdapter instance = new FabricBlockStateAdapter();
    }
}
