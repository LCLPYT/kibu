package work.lclpnet.kibu.hook.mixin;

import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

@Mixin(FrostWalkerEnchantment.class)
public class FrostWalkerEnchantmentMixin {

    @Inject(
            method = "freezeWater",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void kibu$onFreeze(LivingEntity entity, World world, BlockPos blockPos, int level, CallbackInfo ci) {
        if (WorldPhysicsHooks.FROST_WALKER_FREEZE.invoker().onFreeze(world, blockPos, entity))
            ci.cancel();
    }
}
