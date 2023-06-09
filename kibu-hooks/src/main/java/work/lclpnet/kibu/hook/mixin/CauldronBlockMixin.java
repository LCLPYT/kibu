package work.lclpnet.kibu.hook.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

@Mixin(CauldronBlock.class)
public class CauldronBlockMixin {

    @Inject(
            method = "precipitationTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"
            ),
            cancellable = true
    )
    public void kibu$onPrecipitationTick(BlockState state, World world, BlockPos pos, Biome.Precipitation precipitation, CallbackInfo ci) {
        BlockState toState = switch (precipitation) {
            case RAIN -> Blocks.WATER_CAULDRON.getDefaultState();
            case SNOW -> Blocks.POWDER_SNOW_CAULDRON.getDefaultState();
            default -> null;
        };

        if (toState == null) return;

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
        BlockState toState = null;
        if (fluid == Fluids.WATER) {
            toState = Blocks.WATER_CAULDRON.getDefaultState();
        } else if (fluid == Fluids.LAVA) {
            toState = Blocks.LAVA_CAULDRON.getDefaultState();
        }

        if (toState == null) return;

        if (WorldPhysicsHooks.CAULDRON_DRIP_STONE.invoker().onChange(world, pos, toState)) {
            ci.cancel();
        }
    }
}
