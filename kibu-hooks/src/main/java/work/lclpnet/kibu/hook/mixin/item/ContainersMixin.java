package work.lclpnet.kibu.hook.mixin.item;

import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.level.ItemScatterCallback;

@Mixin(Containers.class)
public class ContainersMixin {

    @Inject(
            method = "dropItemStack(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void kibu$onScatter(Level level, double x, double y, double z, ItemStack itemStack, CallbackInfo ci) {
        if (ItemScatterCallback.HOOK.invoker().onScatter(level, x, y, z, itemStack)) {
            ci.cancel();
        }
    }
}
