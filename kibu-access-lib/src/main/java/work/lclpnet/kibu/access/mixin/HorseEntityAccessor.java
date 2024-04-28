package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.passive.HorseColor;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.HorseMarking;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(HorseEntity.class)
public interface HorseEntityAccessor {

    @Invoker
    void invokeSetHorseVariant(HorseColor color, HorseMarking marking);
}
