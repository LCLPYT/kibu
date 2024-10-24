package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.EntityConvertCallback;

@Mixin(ZombieVillagerEntity.class)
public class ZombieVillagerEntityMixin {

    @Inject(
            method = "finishConversion",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/ZombieVillagerEntity;convertTo(Lnet/minecraft/entity/EntityType;Lnet/minecraft/entity/conversion/EntityConversionContext;Lnet/minecraft/entity/conversion/EntityConversionContext$Finalizer;)Lnet/minecraft/entity/mob/MobEntity;"
            ),
            cancellable = true
    )
    public void kibu$onConvert(ServerWorld world, CallbackInfo ci) {
        ZombieVillagerEntity self = (ZombieVillagerEntity) (Object) this;

        if (EntityConvertCallback.HOOK.invoker().onConvert(self, EntityType.VILLAGER)) {
            ci.cancel();
        }
    }
}
