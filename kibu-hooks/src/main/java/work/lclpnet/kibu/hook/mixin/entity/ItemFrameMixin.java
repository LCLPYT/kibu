package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.ItemFramePutItemCallback;
import work.lclpnet.kibu.hook.entity.ItemFrameRemoveItemCallback;
import work.lclpnet.kibu.hook.entity.ItemFrameRotateCallback;

@Mixin(ItemFrame.class)
public class ItemFrameMixin {

    @Inject(
            method = "hurtServer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/decoration/ItemFrame;dropItem(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Z)V"
            ),
            cancellable = true
    )
    public void kibu$beforeDamage(ServerLevel world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ItemFrame self = (ItemFrame) (Object) this;

        if (ItemFrameRemoveItemCallback.HOOK.invoker().onRemoveItem(self, source.getEntity())) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/decoration/ItemFrame;setItem(Lnet/minecraft/world/item/ItemStack;)V"
            ),
            cancellable = true
    )
    public void kibu$onPutIntoFrame(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir, @Local ItemStack stack) {
        ItemFrame self = (ItemFrame) (Object) this;

        if (ItemFramePutItemCallback.HOOK.invoker().onPutIntoFrame(self, stack, player, hand)) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }

    @Inject(
            method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/decoration/ItemFrame;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"
            ),
            cancellable = true
    )
    public void kibu$onRotateFrame(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        ItemFrame self = (ItemFrame) (Object) this;

        if (ItemFrameRotateCallback.HOOK.invoker().onRotateFrame(self, player, hand)) {
            cir.setReturnValue(InteractionResult.FAIL);
        }
    }
}
