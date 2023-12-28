package work.lclpnet.kibu.schematic;

import com.google.common.collect.Streams;
import net.minecraft.SharedConstants;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.mc.KibuBlockPos;
import work.lclpnet.kibu.structure.ArrayBlockStructure;
import work.lclpnet.kibu.structure.BlockStructure;
import work.lclpnet.kibu.structure.SimpleBlockStructure;

import javax.annotation.Nonnull;
import java.util.Objects;

public class FabricStructureWrapper implements FabricStructureView {

    private final BlockStructure structure;
    protected final FabricBlockStateAdapter adapter;

    public FabricStructureWrapper() {
        this(createSimpleStructure());
    }

    public FabricStructureWrapper(BlockStructure structure) {
        this(structure, FabricBlockStateAdapter.getInstance());
    }

    public FabricStructureWrapper(BlockStructure structure, FabricBlockStateAdapter adapter) {
        this.structure = Objects.requireNonNull(structure);
        this.adapter = Objects.requireNonNull(adapter);
    }

    public static SimpleBlockStructure createSimpleStructure() {
        int dataVersion = getDataVersion();
        return new SimpleBlockStructure(dataVersion);
    }

    public static ArrayBlockStructure createArrayStructure(int width, int height, int length, KibuBlockPos origin) {
        int dataVersion = getDataVersion();
        return new ArrayBlockStructure(width, height, length, origin, dataVersion);
    }

    public static int getDataVersion() {
        return SharedConstants.getGameVersion().getSaveVersion().getId();
    }

    @Nonnull
    public BlockStructure getStructure() {
        return structure;
    }

    public void setBlockState(BlockPos pos, BlockState state) {
        structure.setBlockState(adapter.adapt(pos), adapter.adapt(state));
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        var state = structure.getBlockState(adapter.adapt(pos));
        return state != null ? adapter.revert(state) : null;
    }

    @Override
    public Iterable<BlockPos> getBlockPositions() {
        return Streams.stream(structure.getBlockPositions())
                .map(adapter::revert)
                .toList();
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return null;  // no block entities
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return Fluids.EMPTY.getDefaultState();  // no fluids
    }

    @Override
    public int getHeight() {
        return structure.getHeight();
    }

    @Override
    public int getBottomY() {
        return structure.getOrigin().getY();
    }

    public void copyTo(FabricStructureWrapper other) {
        for (BlockPos pos : getBlockPositions()) {
            other.setBlockState(pos, getBlockState(pos));
        }
    }
}
