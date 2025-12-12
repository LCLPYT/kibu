package work.lclpnet.kibu.access.mixin;

import net.minecraft.world.entity.decoration.ArmorStand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ArmorStand.class)
public interface ArmorStandEntityAccessor {

    @Invoker
    void invokeSetSmall(boolean small);

    @Invoker
    void invokeSetMarker(boolean marker);
}
