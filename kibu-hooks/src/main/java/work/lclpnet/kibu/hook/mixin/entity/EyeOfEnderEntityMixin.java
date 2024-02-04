package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.util.MixinUtils;

@Mixin(EyeOfEnderEntity.class)
public class EyeOfEnderEntityMixin {

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    public boolean kibu$onDropItem(World world, Entity entity, Operation<Boolean> original) {
        boolean allowed = MixinUtils.wrapEntityItemDrop(world, entity, original, this);

        if (!allowed) {
            EyeOfEnderEntity self = (EyeOfEnderEntity) (Object) this;

            // display the break animation
            self.getWorld().syncWorldEvent(WorldEvents.EYE_OF_ENDER_BREAKS, self.getBlockPos(), 0);
        }

        return allowed;
    }
}
