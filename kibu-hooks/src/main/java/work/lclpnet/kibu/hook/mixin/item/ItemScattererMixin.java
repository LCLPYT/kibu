package work.lclpnet.kibu.hook.mixin.item;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.world.ItemScatterCallback;

@Mixin(ItemScatterer.class)
public class ItemScattererMixin {

    @Inject(
            method = "spawn(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void kibu$onScatter(World world, double x, double y, double z, ItemStack stack, CallbackInfo ci) {
        if (ItemScatterCallback.HOOK.invoker().onScatter(world, x, y, z, stack)) {
            ci.cancel();
        }
    }
}
