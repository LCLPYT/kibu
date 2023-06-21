package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ArmorStandEntity.class)
public interface ArmorStandEntityAccessor {

    @Invoker
    void invokeSetSmall(boolean small);

    @Invoker
    void invokeSetMarker(boolean marker);
}
