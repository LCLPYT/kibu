package work.lclpnet.kibu.hook.mixin.ai;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.feline.Cat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.util.MixinUtils;

@Mixin(targets = "net.minecraft.world.entity.animal.feline.Cat$CatRelaxOnOwnerGoal")
public class CatEntity$CatRelaxOnOwnerGoalMixin {

    @Shadow @Final private Cat cat;

    @WrapOperation(
            method = "lambda$giveMorningGift$0",  // this is a lambda in dropMorningGifts(), naming may change in the future
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
            )
    )
    public boolean kibu$onDropItem(ServerLevel instance, Entity entity, Operation<Boolean> original) {
        return MixinUtils.wrapEntityItemDrop(instance, entity, original, this.cat);
    }
}
