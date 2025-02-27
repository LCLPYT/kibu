package work.lclpnet.kibu.inv.mixin;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ScreenHandler.class)
public interface ScreenHandlerAccessor {

    @Accessor
    IntList getTrackedPropertyValues();
}
