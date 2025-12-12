package work.lclpnet.kibu.hook.mixin.access;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerSynchronizer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerMenu.class)
public interface ScreenHandlerAccessor {

    @Nullable
    @Accessor
    ContainerSynchronizer getSynchronizer();
}
