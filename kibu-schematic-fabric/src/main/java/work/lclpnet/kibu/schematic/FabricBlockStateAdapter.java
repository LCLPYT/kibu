package work.lclpnet.kibu.schematic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.mc.*;
import work.lclpnet.kibu.nbt.FabricNbtConversion;
import work.lclpnet.kibu.util.BlockStateUtils;

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
        ResourceLocation id = ResourceLocation.tryParse(blockEntity.getId());

        return BuiltInRegistries.BLOCK_ENTITY_TYPE.getOptional(id).map(type -> {
            BlockPos pos = revert(blockEntity.getPosition());
            CompoundTag nbt = FabricNbtConversion.convert(blockEntity.createNbt(), CompoundTag.class);

            return new FabricKibuBlockEntity(type, pos, nbt);
        });
    }

    public KibuEntity adapt(Entity entity) {
        return new FabricKibuEntity(entity);
    }

    public Optional<FabricKibuEntity> revert(KibuEntity entity) {
        return EntityType.byString(entity.getId()).map(type -> {
            Vec3 pos = new Vec3(entity.getX(), entity.getY(), entity.getZ());
            CompoundTag nbt = FabricNbtConversion.convert(entity.getExtraNbt(), CompoundTag.class);

            return new FabricKibuEntity(type, pos, nbt);
        });
    }

    private static final class InstanceHolder {
        private static final FabricBlockStateAdapter instance = new FabricBlockStateAdapter();
    }
}
