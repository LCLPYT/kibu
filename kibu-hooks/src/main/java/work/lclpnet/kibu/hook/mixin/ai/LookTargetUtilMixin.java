package work.lclpnet.kibu.hook.mixin.ai;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.entity.EntityDropItemCallback;

@Mixin(LookTargetUtil.class)
public class LookTargetUtilMixin {

    @WrapOperation(
            method = "give(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    private static boolean kibu$onDropItem(World world, Entity entity, Operation<Boolean> original, @Local(argsOnly = true) LivingEntity thrower) {
        if (entity instanceof ItemEntity itemEntity) {
            if (EntityDropItemCallback.HOOK.invoker().onDropItem(world, thrower, itemEntity)) {
                // cancelled, do not call original
                return false;
            }
        }

        return original.call(world, entity);
    }

}
