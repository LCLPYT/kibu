package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.entity.EntityConvertCallback;

@Mixin(Zombie.class)
public class ZombieMixin {

    @Inject(
            method = "doUnderWaterConversion",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onConvertToDrowned(CallbackInfo ci) {
        Zombie self = (Zombie) (Object) this;

        if (EntityConvertCallback.HOOK.invoker().onConvert(self, EntityType.DROWNED)) {
            ci.cancel();
        }
    }

    @WrapOperation(
            method = "convertVillagerToZombieVillager",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/npc/villager/Villager;convertTo(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/ConversionParams;Lnet/minecraft/world/entity/ConversionParams$AfterConversion;)Lnet/minecraft/world/entity/Mob;"
            )
    )
    public <T extends Mob> Mob kibu$onInfestVillager(Villager instance, EntityType<T> entityType, ConversionParams entityConversionContext, ConversionParams.AfterConversion<T> finalizer, Operation<Mob> original) {
        if (EntityConvertCallback.HOOK.invoker().onConvert(instance, entityType)) {
            return null;
        }

        return original.call(instance, entityType, entityConversionContext, finalizer);
    }
}
