package work.lclpnet.kibu.hook.mixin.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.TurtleEggBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

@Mixin(TurtleEggBlock.class)
public class TurtleEggBlockMixin {

    @Inject(
            method = "tryBreakEgg",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/TurtleEggBlock;breakEgg(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"
            ),
            cancellable = true
    )
    public void kibu$onBreakEgg(World world, BlockState state, BlockPos pos, Entity entity, int inverseChance, CallbackInfo ci) {
        if (BlockModificationHooks.TRAMPLE_TURTLE_EGG.invoker().onModify(world, pos, entity)) {
            ci.cancel();
        }
    }
}
