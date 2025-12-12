package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.EyeOfEnder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.util.MixinUtils;

@Mixin(EyeOfEnder.class)
public class EyeOfEnderMixin {

    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
            )
    )
    public boolean kibu$onDropItem(Level world, Entity entity, Operation<Boolean> original) {
        boolean allowed = MixinUtils.wrapEntityItemDrop(world, entity, original, this);

        if (!allowed) {
            EyeOfEnder self = (EyeOfEnder) (Object) this;

            // display the break animation
            self.level().levelEvent(LevelEvent.PARTICLES_EYE_OF_ENDER_DEATH, self.blockPosition(), 0);
        }

        return allowed;
    }
}
