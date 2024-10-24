package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ModelTransformationMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DisplayEntity.ItemDisplayEntity.class)
public interface ItemDisplayEntityAccessor {

    @Invoker
    void invokeSetItemStack(ItemStack stack);

    @Invoker
    ItemStack invokeGetItemStack();

    @Invoker
    void invokeSetTransformationMode(ModelTransformationMode transformationMode);

    @Invoker
    ModelTransformationMode invokeGetTransformationMode();
}
