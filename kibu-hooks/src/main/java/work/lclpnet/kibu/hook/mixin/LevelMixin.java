package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.world.BlockBreakParticleCallback;

@Mixin(Level.class)
public class LevelMixin {

    @WrapOperation(
            method = "destroyBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;levelEvent(ILnet/minecraft/core/BlockPos;I)V"
            )
    )
    public void kibu$onBreakBlockParticles(Level world, int eventId, BlockPos pos, int rawId, Operation<Void> original) {
        BlockState state = world.getBlockState(pos);

        if (BlockBreakParticleCallback.HOOK.invoker().onSpawnParticles(world, pos, state)) return;

        original.call(world, eventId, pos, rawId);
    }
}
