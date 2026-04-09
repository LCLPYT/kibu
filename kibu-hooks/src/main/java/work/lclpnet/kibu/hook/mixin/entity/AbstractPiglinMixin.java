package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.entity.EntityConvertCallback;

@Mixin(AbstractPiglin.class)
public class AbstractPiglinMixin {

    @WrapOperation(
            method = "customServerAiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/piglin/AbstractPiglin;playConvertedSound()V"
            )
    )
    public void kibu$onPlayZombificationSound(AbstractPiglin instance, Operation<Void> original,
                                              @Share("zombify") LocalBooleanRef cancelled) {
        boolean cancel = EntityConvertCallback.HOOK.invoker().onConvert(instance, EntityType.ZOMBIFIED_PIGLIN);
        cancelled.set(cancel);
    }

    @WrapOperation(
            method = "customServerAiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/piglin/AbstractPiglin;finishConversion(Lnet/minecraft/server/level/ServerLevel;)V"
            )
    )
    public void kibu$onZombify(AbstractPiglin instance, ServerLevel level, Operation<Void> original,
                               @Share("zombify") LocalBooleanRef cancelled) {
        if (!cancelled.get()) {
            original.call(instance, level);
        }
    }
}
