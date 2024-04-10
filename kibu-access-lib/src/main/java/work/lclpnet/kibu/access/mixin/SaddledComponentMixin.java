package work.lclpnet.kibu.access.mixin;

import net.minecraft.entity.SaddledComponent;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import work.lclpnet.kibu.access.type.KibuSaddledComponent;

@Mixin(SaddledComponent.class)
public class SaddledComponentMixin implements KibuSaddledComponent {

    @Shadow private boolean boosted;

    @Shadow private int boostedTime;

    @Shadow @Final private DataTracker dataTracker;

    @Shadow @Final private TrackedData<Integer> boostTime;

    @Override
    public void kibu$boost(int ticks) {
        this.boosted = true;
        this.boostedTime = 0;
        this.dataTracker.set(this.boostTime, ticks);
    }
}
