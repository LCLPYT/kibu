package work.lclpnet.kibu.hook.mixin.blockentity;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.type.BlockPosAware;

@Mixin(LecternBlockEntity.class)
public class LecternBlockEntityMixin {

    @Inject(
            method = "createMenu",
            at = @At("RETURN")
    )
    public void kibu$injectBlockPos(int containerId, Inventory inventory, Player player, CallbackInfoReturnable<AbstractContainerMenu> cir) {
        @SuppressWarnings("DataFlowIssue")
        BlockEntity blockEntity = (BlockEntity) (Object) this;

        BlockPosAware handler = (BlockPosAware) cir.getReturnValue();
        handler.kibu$setBlockPos(blockEntity.getBlockPos());
    }
}
