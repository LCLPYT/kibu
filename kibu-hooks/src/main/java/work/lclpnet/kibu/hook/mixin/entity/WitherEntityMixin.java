package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.EntityBossBarCallback;
import work.lclpnet.kibu.hook.entity.WitherShootCallback;

@Mixin(WitherEntity.class)
public class WitherEntityMixin {

    @Inject(
            method = "shootSkullAt(IDDDZ)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onShootSkull(int headIndex, double targetX, double targetY, double targetZ, boolean charged, CallbackInfo ci) {
        WitherEntity self = (WitherEntity) (Object) this;

        if (WitherShootCallback.HOOK.invoker().onShootAt(self, targetX, targetY, targetZ)) {
            ci.cancel();
        }
    }

    @WrapOperation(
            method = "onStartedTrackingBy",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/boss/ServerBossBar;addPlayer(Lnet/minecraft/server/network/ServerPlayerEntity;)V"
            )
    )
    public void kibu$onShowBossBarTo(ServerBossBar instance, ServerPlayerEntity player, Operation<Void> original) {
        WitherEntity self = (WitherEntity) (Object) this;

        if (EntityBossBarCallback.HOOK.invoker().onShow(self, instance, player)) return;

        original.call(instance, player);
    }
}
