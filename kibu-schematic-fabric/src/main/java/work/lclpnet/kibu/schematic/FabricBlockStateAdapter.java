package work.lclpnet.kibu.schematic;

import net.minecraft.util.math.Vec3i;
import work.lclpnet.kibu.mc.BlockPos;
import work.lclpnet.kibu.mc.BlockState;
import work.lclpnet.kibu.mc.BlockStateAdapter;
import work.lclpnet.kibu.util.BlockStateUtils;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class FabricBlockStateAdapter implements BlockStateAdapter {

    private final Map<net.minecraft.block.BlockState, BlockState> states = new HashMap<>();

    protected FabricBlockStateAdapter() {
    }

    public static FabricBlockStateAdapter getInstance() {
        return InstanceHolder.instance;
    }

    @Nullable
    @Override
    public BlockState getBlockState(String string) {
        var nativeBlockState = getNativeBlockState(string);
        return nativeBlockState != null ? adapt(nativeBlockState) : null;
    }

    @Nullable
    public net.minecraft.block.BlockState getNativeBlockState(String string) {
        return BlockStateUtils.parse(string);
    }

    @Nullable
    public net.minecraft.block.BlockState revert(BlockState state) {
        if (state instanceof FabricBlockState fState) {
            return fState.getState();
        }

        // fallback to parsing the string representation (slow)
        return getNativeBlockState(state.getAsString());
    }

    public net.minecraft.util.math.BlockPos revert(BlockPos pos) {
        return new net.minecraft.util.math.BlockPos(pos.getX(), pos.getY(), pos.getZ());
    }

    public BlockState adapt(net.minecraft.block.BlockState state) {
        return states.computeIfAbsent(state, FabricBlockState::new);
    }

    public BlockPos adapt(Vec3i pos) {
        return new BlockPos(pos.getX(), pos.getY(), pos.getZ());
    }

    private static final class InstanceHolder {
        private static final FabricBlockStateAdapter instance = new FabricBlockStateAdapter();
    }
}
