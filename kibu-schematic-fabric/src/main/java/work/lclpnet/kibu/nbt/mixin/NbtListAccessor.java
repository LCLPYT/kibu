package work.lclpnet.kibu.nbt.mixin;

import net.minecraft.nbt.ListTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ListTag.class)
public interface NbtListAccessor {

    @Invoker
    byte invokeIdentifyRawElementType();
}
