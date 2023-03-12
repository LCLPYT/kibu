package work.lclpnet.kibu.hook.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.LecternScreenHandler;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.model.BlockPosAware;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;

@Mixin(LecternScreenHandler.class)
public class LecternScreenHandlerMixin implements BlockPosAware {

    @Unique
    private BlockPos blockPosition = null;

    @Inject(
            method = "onButtonClick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/inventory/Inventory;removeStack(I)Lnet/minecraft/item/ItemStack;"
            ),
            cancellable = true
    )
    public void onTakeBook(PlayerEntity player, int id, CallbackInfoReturnable<Boolean> cir) {
        if (BlockModificationHooks.TAKE_LECTERN_BOOK.invoker().onModify(player.getWorld(), blockPosition, player)) {
            cir.setReturnValue(false);
        }
    }

    @Override
    public void kibu$setBlockPos(BlockPos pos) {
        this.blockPosition = pos;
    }
}
