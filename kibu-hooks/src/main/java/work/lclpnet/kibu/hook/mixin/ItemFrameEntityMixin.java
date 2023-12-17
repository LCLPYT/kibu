package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import work.lclpnet.kibu.hook.entity.ItemFrameDamageCallback;
import work.lclpnet.kibu.hook.entity.ItemFramePutItemCallback;
import work.lclpnet.kibu.hook.entity.ItemFrameRotateCallback;

@Mixin(ItemFrameEntity.class)
public class ItemFrameEntityMixin {

    @Inject(
            method = "damage",
            at = @At("HEAD"),
            cancellable = true
    )
    public void kibu$beforeDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ItemFrameEntity self = (ItemFrameEntity) (Object) this;

        if (ItemFrameDamageCallback.HOOK.invoker().onDamage(self, source, amount)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;setHeldItemStack(Lnet/minecraft/item/ItemStack;)V"
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void kibu$onPutIntoFrame(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir, ItemStack stack) {
        ItemFrameEntity self = (ItemFrameEntity) (Object) this;

        if (ItemFramePutItemCallback.HOOK.invoker().onPutIntoFrame(self, stack, player, hand)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(
            method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"
            ),
            cancellable = true
    )
    public void kibu$onRotateFrame(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemFrameEntity self = (ItemFrameEntity) (Object) this;

        if (ItemFrameRotateCallback.HOOK.invoker().onRotateFrame(self, player, hand)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}
