package work.lclpnet.kibu.access.mixin;

import net.minecraft.world.entity.Display;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.ItemDisplay.class)
public interface ItemDisplayEntityAccessor {

    @Invoker
    void invokeSetItemStack(ItemStack stack);

    @Invoker
    ItemStack invokeGetItemStack();

    @Invoker
    void invokeSetItemTransform(ItemDisplayContext itemDisplayContext);

    @Invoker
    ItemDisplayContext invokeGetItemTransform();
}
