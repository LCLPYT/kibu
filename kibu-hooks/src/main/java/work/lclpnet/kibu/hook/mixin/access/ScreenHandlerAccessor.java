package work.lclpnet.kibu.hook.mixin.access;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerSyncHandler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ScreenHandler.class)
public interface ScreenHandlerAccessor {

    @Nullable
    @Accessor
    ScreenHandlerSyncHandler getSyncHandler();
}
