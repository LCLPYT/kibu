package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.ArmorStandManipulateCallback;

@Mixin(ArmorStandEntity.class)
public class ArmorStandEntityMixin {

    @Inject(
            method = "equip",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$beforeEquip(PlayerEntity player, EquipmentSlot slot, ItemStack stack, Hand hand, CallbackInfoReturnable<Boolean> cir) {
        ArmorStandEntity self = (ArmorStandEntity) (Object) this;

        if (ArmorStandManipulateCallback.HOOK.invoker().onManipulate(self, player, slot, stack, hand)) {
            cir.setReturnValue(false);
        }
    }
}
