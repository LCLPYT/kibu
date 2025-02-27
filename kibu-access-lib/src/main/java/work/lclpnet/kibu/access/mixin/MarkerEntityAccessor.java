package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.MarkerEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MarkerEntity.class)
public interface MarkerEntityAccessor {

    @Accessor
    NbtCompound getData();
}
