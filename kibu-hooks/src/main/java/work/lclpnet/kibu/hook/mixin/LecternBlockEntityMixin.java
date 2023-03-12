package work.lclpnet.kibu.hook.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import work.lclpnet.kibu.hook.model.BlockPosAware;

@Mixin(LecternBlockEntity.class)
public class LecternBlockEntityMixin {

    @Inject(
            method = "createMenu",
            at = @At("RETURN")
    )
    public void kibu$injectBlockPos(int i, PlayerInventory playerInventory, PlayerEntity playerEntity, CallbackInfoReturnable<ScreenHandler> cir) {
        @SuppressWarnings("DataFlowIssue")
        BlockEntity blockEntity = (BlockEntity) (Object) this;

        BlockPosAware handler = (BlockPosAware) cir.getReturnValue();
        handler.kibu$setBlockPos(blockEntity.getPos());
    }
}
