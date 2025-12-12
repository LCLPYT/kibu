package work.lclpnet.kibu.access.entity;

import net.minecraft.world.entity.ItemBasedSteering;
import net.minecraft.world.entity.monster.Strider;
import work.lclpnet.kibu.access.mixin.PigAccessor;
import work.lclpnet.kibu.access.type.KibuSaddledComponent;

public class StriderEntityAccess {

    private StriderEntityAccess() {}

    public static void boost(Strider strider, int ticks) {
        ItemBasedSteering component = ((PigAccessor) strider).getSteering();
        ((KibuSaddledComponent) component).kibu$boost(ticks);
    }
}
