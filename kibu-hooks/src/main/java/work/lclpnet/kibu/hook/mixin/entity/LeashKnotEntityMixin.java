package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.entity.leash.LeashEntitiesToBlockCallback;
import work.lclpnet.kibu.hook.entity.leash.LeashKnotTakeCallback;

import java.util.ArrayList;
import java.util.List;

@Mixin(LeashFenceKnotEntity.class)
public abstract class LeashKnotEntityMixin {

    @WrapOperation(
            method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Leashable;leashableLeashedTo(Lnet/minecraft/world/entity/Entity;)Ljava/util/List;",
                    ordinal = 0
            )
    )
    public List<Leashable> kibu$collectEntitiesToLeashToBlock(Entity leashHolder, Operation<List<Leashable>> original,
                                                              @Local(argsOnly = true) Player player) {
        List<Leashable> list = original.call(leashHolder);
        List<Entity> entities = new ArrayList<>(list.size());

        for (Leashable leashable : list) {
            if (leashable instanceof Entity entity) {
                entities.add(entity);
            }
        }

        var self = (LeashFenceKnotEntity) (Object) this;
        BlockPos pos = self.blockPosition();

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
                    target = "Lnet/minecraft/world/entity/Leashable;leashableLeashedTo(Lnet/minecraft/world/entity/Entity;)Ljava/util/List;",
                    ordinal = 1
            )
    )
    public List<Leashable> kibu$collectEntitiesToTakeHoldOf(Entity leashHolder, Operation<List<Leashable>> original,
                                                            @Local(argsOnly = true) Player player) {

        var self = (LeashFenceKnotEntity) (Object) this;

        if (LeashKnotTakeCallback.HOOK.invoker().onTakeHold(player, self)) {
            return List.of();
        }

        return original.call(leashHolder);
    }
}
