package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.util.DyeColor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LlamaEntity.class)
public interface LlamaEntityAccessor {

    @Invoker
    void invokeSetCarpetColor(@Nullable DyeColor color);
}
