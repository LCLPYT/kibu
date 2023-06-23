package work.lclpnet.kibu.hook.mixin;

import net.minecraft.block.WeightedPressurePlateBlock;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.mixin.access.AbstractPressurePlateBlockAccessor;
import work.lclpnet.kibu.hook.world.PressurePlateCallback;

@Mixin(WeightedPressurePlateBlock.class)
public class WeightedPressurePlateBlockMixin {

    @Shadow @Final private int weight;

    @Inject(
            method = "getRedstoneOutput(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)I",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$onGetWeightedRedstoneOutput(World world, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        Box box = AbstractPressurePlateBlockAccessor.getBox().offset(pos);

        var entities = world.getEntitiesByClass(Entity.class, box, EntityPredicates.EXCEPT_SPECTATOR
                .and((entity) -> !entity.canAvoidTraps()));

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

        float f = (float) Math.min(this.weight, i) / (float) this.weight;
        cir.setReturnValue(MathHelper.ceil(f * 15.0F));
    }
}
