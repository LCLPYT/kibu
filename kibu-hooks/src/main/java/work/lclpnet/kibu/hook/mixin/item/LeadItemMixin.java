package work.lclpnet.kibu.hook.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.LeadItem;
import net.minecraft.world.level.Level;
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
            method = "bindPlayerMobs",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;iterator()Ljava/util/Iterator;"
            ),
            cancellable = true
    )
    private static void kibu$attachToBlock(Player player, Level level, BlockPos pos, CallbackInfoReturnable<InteractionResult> cir,
                                           @Local(name = "entitiesToLeash") List<Leashable> entitiesToLeash) {

        if (entitiesToLeash.isEmpty()) return;

        List<Entity> entities = new ArrayList<>(entitiesToLeash.size());

        for (Leashable leashable : entitiesToLeash) {
            if (leashable instanceof Entity entity) {
                entities.add(entity);
            }
        }

        if (LeashEntitiesToBlockCallback.HOOK.invoker().onLeashToBlock(player, pos, entities)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
