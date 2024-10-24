package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.world.BlockBreakParticleCallback;

@Mixin(World.class)
public class WorldMixin {

    @WrapOperation(
            method = "breakBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V"
            )
    )
    public void kibu$onBreakBlockParticles(World world, int eventId, BlockPos pos, int rawId, Operation<Void> original) {
        BlockState state = world.getBlockState(pos);

        if (BlockBreakParticleCallback.HOOK.invoker().onSpawnParticles(world, pos, state)) return;

        original.call(world, eventId, pos, rawId);
    }
}
