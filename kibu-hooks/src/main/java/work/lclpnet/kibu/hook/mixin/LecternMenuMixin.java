package work.lclpnet.kibu.hook.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.LecternMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.level.BlockModificationHooks;
import work.lclpnet.kibu.hook.type.BlockPosAware;

@Mixin(LecternMenu.class)
public class LecternMenuMixin implements BlockPosAware {

    @Unique
    private BlockPos blockPosition = null;

    @Inject(
            method = "clickMenuButton",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/Container;removeItemNoUpdate(I)Lnet/minecraft/world/item/ItemStack;"
            ),
            cancellable = true
    )
    public void kibu$onTakeBook(Player player, int buttonId, CallbackInfoReturnable<Boolean> cir) {
        if (BlockModificationHooks.TAKE_LECTERN_BOOK.invoker().onModify(player.level(), blockPosition, player)) {
            cir.setReturnValue(false);
        }
    }

    @Override
    public void kibu$setBlockPos(BlockPos pos) {
        this.blockPosition = pos;
    }
}
