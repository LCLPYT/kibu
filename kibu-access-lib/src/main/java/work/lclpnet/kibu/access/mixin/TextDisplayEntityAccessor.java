package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DisplayEntity.TextDisplayEntity.class)
public interface TextDisplayEntityAccessor {

    @Invoker
    void invokeSetDisplayFlags(byte flags);

    @Invoker
    byte invokeGetDisplayFlags();

    @Invoker
    void invokeSetBackground(int background);

    @Invoker
    int invokeGetBackground();

    @Invoker
    void invokeSetTextOpacity(byte textOpacity);

    @Invoker
    byte invokeGetTextOpacity();

    @Invoker
    void invokeSetLineWidth(int lineWidth);

    @Invoker
    int invokeGetLineWidth();

    @Invoker
    void invokeSetText(Text text);

    @Invoker
    Text invokeGetText();
}
