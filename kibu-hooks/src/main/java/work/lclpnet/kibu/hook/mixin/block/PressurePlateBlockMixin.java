package work.lclpnet.kibu.hook.mixin.block;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.mixin.access.AbstractPressurePlateBlockAccessor;
import work.lclpnet.kibu.hook.world.PressurePlateCallback;

@Mixin(PressurePlateBlock.class)
public class PressurePlateBlockMixin {

    @Inject(
            method = "getRedstoneOutput(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)I",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/PressurePlateBlock;getEntityCount(Lnet/minecraft/world/World;Lnet/minecraft/util/math/Box;Ljava/lang/Class;)I"
            ),
            cancellable = true
    )
    public void kibu$onGetRedstoneOutput(World world, BlockPos pos, CallbackInfoReturnable<Integer> cir,
                                         @Local Class<? extends Entity> entityClass) {
        Box box = AbstractPressurePlateBlockAccessor.getBox().offset(pos);

        var entities = world.getEntitiesByClass(entityClass, box, EntityPredicates.EXCEPT_SPECTATOR
                .and((entity) -> !entity.canAvoidTraps()));

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
