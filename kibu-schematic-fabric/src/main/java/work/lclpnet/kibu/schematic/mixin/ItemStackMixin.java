package work.lclpnet.kibu.schematic.mixin;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(
            method = "method_57377",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void kibu$suppressEmptyItemError(String error, CallbackInfo ci) {
        // this error is thrown when trying to load nbt with the old item format (e.g. from schematics)
        if (error.equals("Item must not be minecraft:air")) {
            ci.cancel();
        }
    }
}
