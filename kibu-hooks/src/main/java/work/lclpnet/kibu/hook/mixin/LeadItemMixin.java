package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.LeadItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.LeashAttachCallback;
import work.lclpnet.kibu.hook.entity.LeashEntityToBlockCallback;

@Mixin(LeadItem.class)
public class LeadItemMixin {

    @Inject(
            method = "attachHeldMobsToBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/decoration/LeashKnotEntity;getOrCreate(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/entity/decoration/LeashKnotEntity;"
            ),
            cancellable = true
    )
    private static void kibu$beforePlaceLeashKnot(PlayerEntity player, World world, BlockPos pos, CallbackInfoReturnable<ActionResult> cir) {
        if (LeashAttachCallback.HOOK.invoker().onAttach(player, world, pos)) {
            cir.setReturnValue(ActionResult.PASS);
        }
    }

    @WrapWithCondition(
            method = "attachHeldMobsToBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/mob/MobEntity;attachLeash(Lnet/minecraft/entity/Entity;Z)V"
            )
    )
    private static boolean kibu$attachLeashAllowed(MobEntity instance, Entity entity, boolean sendPacket) {
        Entity holder = instance.getHoldingEntity();
        if (!(holder instanceof PlayerEntity player) || !(entity instanceof LeashKnotEntity leashKnot)) return true;

        return !LeashEntityToBlockCallback.HOOK.invoker().onLeashToBlock(player, instance, leashKnot);
    }
}
