package work.lclpnet.kibu.access.entity;

import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.entity.animal.Pig;
import work.lclpnet.kibu.access.mixin.PigAccessor;
import work.lclpnet.kibu.access.type.KibuSaddledComponent;

public class PigEntityAccess {

    private PigEntityAccess() {}

    public static void boost(Pig pig, int ticks) {
        ItemBasedSteering component = ((PigAccessor) pig).getSteering();
        ((KibuSaddledComponent) component).kibu$boost(ticks);
    }
}
