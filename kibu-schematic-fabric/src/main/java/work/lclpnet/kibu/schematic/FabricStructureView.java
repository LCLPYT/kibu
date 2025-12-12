package work.lclpnet.kibu.schematic;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface FabricStructureView extends BlockGetter {

    BlockState getBlockState(BlockPos pos);

    Iterable<BlockPos> getBlockPositions();
}
