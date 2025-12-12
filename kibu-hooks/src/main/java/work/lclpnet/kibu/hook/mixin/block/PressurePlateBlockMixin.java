package work.lclpnet.kibu.hook.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.mixin.access.BasePressurePlateBlockAccessor;
import work.lclpnet.kibu.hook.world.PressurePlateCallback;

@Mixin(PressurePlateBlock.class)
public class PressurePlateBlockMixin {

    @Inject(
            method = "getSignalStrength(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/PressurePlateBlock;getEntityCount(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/AABB;Ljava/lang/Class;)I"
            ),
            cancellable = true
    )
    public void kibu$onGetRedstoneOutput(Level world, BlockPos pos, CallbackInfoReturnable<Integer> cir,
                                         @Local Class<? extends Entity> entityClass) {
        AABB box = BasePressurePlateBlockAccessor.getBox().move(pos);

        var entities = world.getEntitiesOfClass(entityClass, box, EntitySelector.NO_SPECTATORS
                .and((entity) -> !entity.isIgnoringBlockTriggers()));

        boolean success = false;
        boolean modified = false;

        for (Entity entity : entities) {
            if (PressurePlateCallback.HOOK.invoker().onPress(world, pos, entity)) {
                modified = true;
            } else {
                success = true;
            }
        }

        if (!modified) return;

        cir.setReturnValue(success ? 15 : 0);
    }
}
