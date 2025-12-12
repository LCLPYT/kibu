package work.lclpnet.kibu.access.mixin;

import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.entity.monster.Strider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Strider.class)
public interface StriderEntityAccessor {

    @Accessor
    ItemBasedSteering getSteering();
}
