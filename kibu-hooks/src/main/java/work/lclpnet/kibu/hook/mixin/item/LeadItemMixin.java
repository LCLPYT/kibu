package work.lclpnet.kibu.hook.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.LeadItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.leash.LeashEntitiesToBlockCallback;

import java.util.ArrayList;
import java.util.List;

@Mixin(LeadItem.class)
public class LeadItemMixin {

    @Inject(
            method = "attachHeldMobsToBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;iterator()Ljava/util/Iterator;"
            ),
            cancellable = true
    )
    private static void kibu$attachToBlock(PlayerEntity player, World world, BlockPos pos, CallbackInfoReturnable<ActionResult> cir,
                                           @Local List<Leashable> list) {

        if (list.isEmpty()) return;

        List<Entity> entities = new ArrayList<>(list.size());

        for (Leashable leashable : list) {
            if (leashable instanceof Entity entity) {
                entities.add(entity);
            }
        }

        if (LeashEntitiesToBlockCallback.HOOK.invoker().onLeashToBlock(player, pos, entities)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }
}
