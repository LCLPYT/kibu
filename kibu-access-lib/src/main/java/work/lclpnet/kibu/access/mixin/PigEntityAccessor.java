package work.lclpnet.kibu.access.mixin;

import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.entity.animal.Pig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Pig.class)
public interface PigEntityAccessor {

    @Accessor
    ItemBasedSteering getSteering();
}
