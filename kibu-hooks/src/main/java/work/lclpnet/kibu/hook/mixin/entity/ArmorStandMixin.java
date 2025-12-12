package work.lclpnet.kibu.hook.mixin.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.ArmorStandManipulateCallback;
import work.lclpnet.kibu.hook.entity.NonLivingDamageCallback;

@Mixin(ArmorStand.class)
public class ArmorStandMixin {

    @Inject(
            method = "swapItem",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$beforeEquip(Player player, EquipmentSlot slot, ItemStack stack, InteractionHand hand, CallbackInfoReturnable<Boolean> cir) {
        ArmorStand self = (ArmorStand) (Object) this;

        if (ArmorStandManipulateCallback.HOOK.invoker().onManipulate(self, player, slot, stack, hand)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "hurtServer",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$beforeDamage(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ArmorStand self = (ArmorStand) (Object) this;

        if (NonLivingDamageCallback.HOOK.invoker().onDamage(self, source, amount)) {
            cir.setReturnValue(false);
        }
    }
}
