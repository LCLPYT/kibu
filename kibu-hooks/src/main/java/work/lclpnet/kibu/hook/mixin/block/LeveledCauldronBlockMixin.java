package work.lclpnet.kibu.hook.mixin.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

import static net.minecraft.block.LeveledCauldronBlock.LEVEL;

@Mixin(LeveledCauldronBlock.class)
public class LeveledCauldronBlockMixin {

    @Inject(
            method = "precipitationTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"
            ),
            cancellable = true
    )
    public void kibu$onPrecipitationTick(BlockState state, World world, BlockPos pos, Biome.Precipitation precipitation, CallbackInfo ci) {
        BlockState toState = state.cycle(LEVEL);

        if (WorldPhysicsHooks.CAULDRON_PRECIPITATION.invoker().onChange(world, pos, toState)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "fillFromDripstone",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"
            ),
            cancellable = true
    )
    public void kibu$onFillFromDripstone(BlockState state, World world, BlockPos pos, Fluid fluid, CallbackInfo ci) {
        BlockState toState = state.with(LEVEL, state.get(LEVEL) + 1);

        if (WorldPhysicsHooks.CAULDRON_DRIP_STONE.invoker().onChange(world, pos, toState)) {
            ci.cancel();
        }
    }
}
