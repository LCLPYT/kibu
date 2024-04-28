package work.lclpnet.kibu.access.entity;

import net.minecraft.entity.SaddledComponent;
import net.minecraft.entity.passive.PigEntity;
import work.lclpnet.kibu.access.mixin.PigEntityAccessor;
import work.lclpnet.kibu.access.type.KibuSaddledComponent;

public class PigEntityAccess {

    private PigEntityAccess() {}

    public static void setSaddled(PigEntity pig, boolean saddled) {
        ((PigEntityAccessor) pig).getSaddledComponent().setSaddled(saddled);
    }

    public static void boost(PigEntity pig, int ticks) {
        SaddledComponent component = ((PigEntityAccessor) pig).getSaddledComponent();
        ((KibuSaddledComponent) component).kibu$boost(ticks);
    }
}
