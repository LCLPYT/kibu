package work.lclpnet.kibu.hook.mixin.ai;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.util.MixinUtils;

@Mixin(targets = "net.minecraft.world.entity.animal.dolphin.Dolphin$PlayWithItemsGoal")
public class DolphinEntity$PlayWithItemsGoalMixin {

    @Shadow @Final
    Dolphin this$0;  // reference to the outer class, naming may change in the future

    @WrapOperation(
            method = "drop",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
            )
    )
    public boolean kibu$onDropItem(Level world, Entity entity, Operation<Boolean> original) {
        return MixinUtils.wrapEntityItemDrop(world, entity, original, this.this$0);
    }
}
