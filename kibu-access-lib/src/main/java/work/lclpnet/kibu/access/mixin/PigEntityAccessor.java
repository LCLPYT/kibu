package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.SaddledComponent;
import net.minecraft.entity.passive.PigEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PigEntity.class)
public interface PigEntityAccessor {

    @Accessor
    SaddledComponent getSaddledComponent();
}
