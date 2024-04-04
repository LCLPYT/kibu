package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.entity.EntityConvertCallback;

@Mixin(AbstractPiglinEntity.class)
public class AbstractPiglinEntityMixin {

    @WrapOperation(
            method = "mobTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/AbstractPiglinEntity;playZombificationSound()V"
            )
    )
    public void kibu$onPlayZombificationSound(AbstractPiglinEntity instance, Operation<Void> original,
                                              @Share("zombify") LocalBooleanRef cancelled) {
        boolean cancel = EntityConvertCallback.HOOK.invoker().onConvert(instance, EntityType.ZOMBIFIED_PIGLIN);
        cancelled.set(cancel);
    }

    @WrapOperation(
            method = "mobTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/AbstractPiglinEntity;zombify(Lnet/minecraft/server/world/ServerWorld;)V"
            )
    )
    public void kibu$onZombify(AbstractPiglinEntity instance, ServerWorld world, Operation<Void> original,
                               @Share("zombify") LocalBooleanRef cancelled) {
        if (!cancelled.get()) {
            original.call(instance, world);
        }
    }
}
