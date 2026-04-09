package work.lclpnet.kibu.hook.mixin;

import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.EntityStatusEffectCallback;

@Mixin(MobEffectUtil.class)
public class MobEffectUtilMixin {

    @Inject(
            method = "lambda$addEffectToPlayersAround$0",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void kibu$onTargetPlayerForStatusEffect(
            Entity source, Vec3 position, double radius, Holder<MobEffect> effect,
            MobEffectInstance effectInstance, int displayEffectLimit, ServerPlayer input,
            CallbackInfoReturnable<Boolean> cir) {

        if (!cir.getReturnValueZ()) return;

        // player would be targeted
        if (EntityStatusEffectCallback.HOOK.invoker().onAddEffect(input, effectInstance, source)) {
            cir.setReturnValue(false);
        }
    }
}
