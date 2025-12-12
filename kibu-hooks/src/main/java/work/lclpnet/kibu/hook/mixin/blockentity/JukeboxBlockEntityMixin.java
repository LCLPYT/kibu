package work.lclpnet.kibu.hook.mixin.blockentity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.util.MixinUtils;

@Mixin(JukeboxBlockEntity.class)
public class JukeboxBlockEntityMixin {

    @WrapOperation(
            method = "popOutTheItem",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
            )
    )
    public boolean kibu$onDropItem(Level world, Entity entity, Operation<Boolean> original) {
        return MixinUtils.wrapBlockEntityItemDrop(world, entity, original, this);
    }
}
