package work.lclpnet.kibu.nbt.mixin;

import net.minecraft.nbt.NbtList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NbtList.class)
public interface NbtListAccessor {

    @Invoker
    byte invokeGetValueType();
}
