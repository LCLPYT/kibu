package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.SaddledComponent;
import net.minecraft.entity.passive.StriderEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StriderEntity.class)
public interface StriderEntityAccessor {

    @Accessor
    SaddledComponent getSaddledComponent();
}
