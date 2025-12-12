package work.lclpnet.kibu.access.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Display.TextDisplay.class)
public interface TextDisplayAccessor {

    @Invoker
    void invokeSetFlags(byte flags);

    @Invoker
    byte invokeGetFlags();

    @Invoker
    void invokeSetBackgroundColor(int background);

    @Invoker
    int invokeGetBackgroundColor();

    @Invoker
    void invokeSetTextOpacity(byte textOpacity);

    @Invoker
    byte invokeGetTextOpacity();

    @Invoker
    void invokeSetLineWidth(int lineWidth);

    @Invoker
    int invokeGetLineWidth();

    @Invoker
    void invokeSetText(Component text);

    @Invoker
    Component invokeGetText();
}
