package work.lclpnet.kibu.access.entity;

import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.Nullable;
import work.lclpnet.kibu.access.mixin.LlamaEntityAccessor;

public class LlamaEntityAccess {

    private LlamaEntityAccess() {}

    public static void setCarpetColor(LlamaEntity llama, @Nullable DyeColor color) {
        ((LlamaEntityAccessor) llama).invokeSetCarpetColor(color);
    }
}
