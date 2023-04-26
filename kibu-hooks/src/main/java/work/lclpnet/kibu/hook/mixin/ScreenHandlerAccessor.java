package work.lclpnet.kibu.hook.mixin;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerSyncHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import javax.annotation.Nullable;

@Mixin(ScreenHandler.class)
public interface ScreenHandlerAccessor {

    @Nullable
    @Accessor
    ScreenHandlerSyncHandler getSyncHandler();
}
