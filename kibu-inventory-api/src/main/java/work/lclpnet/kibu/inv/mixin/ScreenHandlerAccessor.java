package work.lclpnet.kibu.inv.mixin;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerMenu.class)
public interface ScreenHandlerAccessor {

    @Accessor
    IntList getRemoteDataSlots();
}
