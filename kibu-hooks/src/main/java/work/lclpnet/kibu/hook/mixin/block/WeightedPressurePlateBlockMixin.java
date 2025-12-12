package work.lclpnet.kibu.hook.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.mixin.access.BasePressurePlateBlockAccessor;
import work.lclpnet.kibu.hook.world.PressurePlateCallback;

@Mixin(WeightedPressurePlateBlock.class)
public class WeightedPressurePlateBlockMixin {

    @Shadow @Final private int maxWeight;

    @Inject(
            method = "getSignalStrength(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)I",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onGetWeightedRedstoneOutput(Level world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        AABB box = BasePressurePlateBlockAccessor.getBox().move(pos);

        var entities = world.getEntitiesOfClass(Entity.class, box, EntitySelector.NO_SPECTATORS
                .and((entity) -> !entity.isIgnoringBlockTriggers()));

        int i = 0;
        boolean modified = false;

        for (Entity entity : entities) {
            if (PressurePlateCallback.HOOK.invoker().onPress(world, pos, entity)) {
                modified = true;
            } else {
                i++;
            }
        }

        if (!modified) return;

        if (i <= 0) {
            cir.setReturnValue(0);
            return;
        }

        float f = (float) Math.min(this.maxWeight, i) / (float) this.maxWeight;
        cir.setReturnValue(Mth.ceil(f * 15.0F));
    }
}
