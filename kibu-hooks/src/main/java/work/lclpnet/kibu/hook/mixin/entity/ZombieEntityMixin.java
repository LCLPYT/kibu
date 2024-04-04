package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.VillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.EntityConvertCallback;

@Mixin(ZombieEntity.class)
public class ZombieEntityMixin {

    @Inject(
            method = "convertInWater",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onConvertToDrowned(CallbackInfo ci) {
        ZombieEntity self = (ZombieEntity) (Object) this;

        if (EntityConvertCallback.HOOK.invoker().onConvert(self, EntityType.DROWNED)) {
            ci.cancel();
        }
    }

    @WrapOperation(
            method = "onKilledOther",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/passive/VillagerEntity;convertTo(Lnet/minecraft/entity/EntityType;Z)Lnet/minecraft/entity/mob/MobEntity;"
            )
    )
    public <T extends MobEntity> MobEntity kibu$onInfestVillager(VillagerEntity instance, EntityType<T> entityType, boolean keepEquipment, Operation<MobEntity> original) {
        if (EntityConvertCallback.HOOK.invoker().onConvert(instance, entityType)) {
            return null;
        }

        return original.call(instance, entityType, keepEquipment);
    }
}
