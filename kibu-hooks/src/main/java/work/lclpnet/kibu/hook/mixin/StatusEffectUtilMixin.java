package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.EntityStatusEffectCallback;

@Mixin(StatusEffectUtil.class)
public class StatusEffectUtilMixin {

    @Inject(
            method = "method_42145",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void kibu$onTargetPlayerForStatusEffect(
            Entity entity, Vec3d vec3d, double d, RegistryEntry<StatusEffect> registryEntry,
            StatusEffectInstance statusEffectInstance, int i, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {

        if (!cir.getReturnValueZ()) return;

        // player would be targeted
        if (EntityStatusEffectCallback.HOOK.invoker().onAddEffect(player, statusEffectInstance, entity)) {
            cir.setReturnValue(false);
        }
    }
}
