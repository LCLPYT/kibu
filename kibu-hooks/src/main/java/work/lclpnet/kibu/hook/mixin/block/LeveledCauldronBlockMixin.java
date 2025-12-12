package work.lclpnet.kibu.hook.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

import static net.minecraft.world.level.block.LayeredCauldronBlock.LEVEL;

@Mixin(LayeredCauldronBlock.class)
public class LeveledCauldronBlockMixin {

    @Inject(
            method = "handlePrecipitation",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"
            ),
            cancellable = true
    )
    public void kibu$onPrecipitationTick(BlockState state, Level world, BlockPos pos, Biome.Precipitation precipitation, CallbackInfo ci) {
        BlockState toState = state.cycle(LEVEL);

        if (WorldPhysicsHooks.CAULDRON_PRECIPITATION.invoker().onChange(world, pos, toState)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "receiveStalactiteDrip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"
            ),
            cancellable = true
    )
    public void kibu$onFillFromDripstone(BlockState state, Level world, BlockPos pos, Fluid fluid, CallbackInfo ci) {
        BlockState toState = state.setValue(LEVEL, state.getValue(LEVEL) + 1);

        if (WorldPhysicsHooks.CAULDRON_DRIP_STONE.invoker().onChange(world, pos, toState)) {
            ci.cancel();
        }
    }
}
