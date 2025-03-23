package work.lclpnet.kibu.access.entity;

import net.minecraft.entity.SaddledComponent;
import net.minecraft.entity.passive.StriderEntity;
import work.lclpnet.kibu.access.mixin.PigEntityAccessor;
import work.lclpnet.kibu.access.type.KibuSaddledComponent;

public class StriderEntityAccess {

    private StriderEntityAccess() {}

    public static void boost(StriderEntity strider, int ticks) {
        SaddledComponent component = ((PigEntityAccessor) strider).getSaddledComponent();
        ((KibuSaddledComponent) component).kibu$boost(ticks);
    }
}
