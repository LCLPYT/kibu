package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.entity.leash.LeashEntitiesToBlockCallback;
import work.lclpnet.kibu.hook.entity.leash.LeashKnotTakeCallback;

import java.util.ArrayList;
import java.util.List;

@Mixin(LeashKnotEntity.class)
public abstract class LeashKnotEntityMixin {

    @WrapOperation(
            method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Leashable;collectLeashablesHeldBy(Lnet/minecraft/entity/Entity;)Ljava/util/List;",
                    ordinal = 0
            )
    )
    public List<Leashable> kibu$collectEntitiesToLeashToBlock(Entity leashHolder, Operation<List<Leashable>> original,
                                                              @Local(argsOnly = true) PlayerEntity player) {
        List<Leashable> list = original.call(leashHolder);
        List<Entity> entities = new ArrayList<>(list.size());

        for (Leashable leashable : list) {
            if (leashable instanceof Entity entity) {
                entities.add(entity);
            }
        }

        var self = (LeashKnotEntity) (Object) this;
        BlockPos pos = self.getBlockPos();

        if (LeashEntitiesToBlockCallback.HOOK.invoker().onLeashToBlock(player, pos, entities)) {
            // cancelled, return empty list to iterate in target method
            return List.of();
        }

        return list;
    }

    @WrapOperation(
            method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Leashable;collectLeashablesHeldBy(Lnet/minecraft/entity/Entity;)Ljava/util/List;",
                    ordinal = 1
            )
    )
    public List<Leashable> kibu$collectEntitiesToTakeHoldOf(Entity leashHolder, Operation<List<Leashable>> original,
                                                            @Local(argsOnly = true) PlayerEntity player) {

        var self = (LeashKnotEntity) (Object) this;

        if (LeashKnotTakeCallback.HOOK.invoker().onTakeHold(player, self)) {
            return List.of();
        }

        return original.call(leashHolder);
    }
}
