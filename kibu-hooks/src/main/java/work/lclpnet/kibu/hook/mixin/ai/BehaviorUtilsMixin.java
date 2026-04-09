package work.lclpnet.kibu.hook.mixin.ai;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.entity.EntityDropItemCallback;

@Mixin(BehaviorUtils.class)
public class BehaviorUtilsMixin {

    @WrapOperation(
            method = "throwItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
            )
    )
    private static boolean kibu$onDropItem(Level world, Entity entity, Operation<Boolean> original, @Local(argsOnly = true, name = "thrower") LivingEntity thrower) {
        if (entity instanceof ItemEntity itemEntity) {
            if (EntityDropItemCallback.HOOK.invoker().onDropItem(world, thrower, itemEntity)) {
                // cancelled, do not call original
                return false;
            }
        }

        return original.call(world, entity);
    }
}
