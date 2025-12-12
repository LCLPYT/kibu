package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.EntityDamageCallback;
import work.lclpnet.kibu.hook.type.PlayerAware;

@Mixin(Player.class)
public class PlayerEntityMixin {

    @Shadow protected FoodData foodData;

    @Inject(
            method = "<init>*",
            at = @At("RETURN")
    )
    public void kibu$onInit(CallbackInfo ci) {
        //noinspection DataFlowIssue
        ((PlayerAware) foodData).kibu$setPlayer((Player) (Object) this);
    }

    @Inject(
            method = "actuallyHurt",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"
            ),
            cancellable = true
    )
    public void kibu$onDamage(ServerLevel world, DamageSource source, float amount, CallbackInfo ci) {
        @SuppressWarnings("DataFlowIssue")
        LivingEntity entity = (LivingEntity) (Object) this;

        if (EntityDamageCallback.HOOK.invoker().onDamage(entity, source, amount)) {
            ci.cancel();
        }
    }
}
