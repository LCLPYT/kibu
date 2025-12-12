package work.lclpnet.kibu.hook.mixin.blockentity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.util.MixinUtils;

@Mixin(BrushableBlockEntity.class)
public class BrushableBlockEntityMixin {

    @WrapOperation(
            method = "dropContent",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
            )
    )
    public boolean kibu$onDropItem(ServerLevel world, Entity entity, Operation<Boolean> original) {
        return MixinUtils.wrapBlockEntityItemDrop(world, entity, original, this);
    }
}
