package work.lclpnet.kibu.hook.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.entity.ItemFramePutItemCallback;
import work.lclpnet.kibu.hook.entity.ItemFrameRemoveItemCallback;
import work.lclpnet.kibu.hook.entity.ItemFrameRotateCallback;

@Mixin(ItemFrameEntity.class)
public class ItemFrameEntityMixin {

    @Inject(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;dropHeldStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/Entity;Z)V"
            ),
            cancellable = true
    )
    public void kibu$beforeDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ItemFrameEntity self = (ItemFrameEntity) (Object) this;

        if (ItemFrameRemoveItemCallback.HOOK.invoker().onRemoveItem(self, source.getAttacker())) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
            method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/decoration/ItemFrameEntity;setHeldItemStack(Lnet/minecraft/item/ItemStack;)V"
            ),
            cancellable = true
    )
    public void kibu$onPutIntoFrame(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir, @Local ItemStack stack) {
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
