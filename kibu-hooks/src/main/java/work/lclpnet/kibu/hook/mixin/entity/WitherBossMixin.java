package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.EntityBossBarCallback;
import work.lclpnet.kibu.hook.entity.WitherShootCallback;

@Mixin(WitherBoss.class)
public class WitherBossMixin {

    @Inject(
            method = "performRangedAttack(IDDDZ)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onShootSkull(int headIndex, double targetX, double targetY, double targetZ, boolean charged, CallbackInfo ci) {
        WitherBoss self = (WitherBoss) (Object) this;

        if (WitherShootCallback.HOOK.invoker().onShootAt(self, targetX, targetY, targetZ)) {
            ci.cancel();
        }
    }

    @WrapOperation(
            method = "startSeenByPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerBossEvent;addPlayer(Lnet/minecraft/server/level/ServerPlayer;)V"
            )
    )
    public void kibu$onShowBossBarTo(ServerBossEvent instance, ServerPlayer player, Operation<Void> original) {
        WitherBoss self = (WitherBoss) (Object) this;

        if (EntityBossBarCallback.HOOK.invoker().onShow(self, instance, player)) return;

        original.call(instance, player);
    }
}
