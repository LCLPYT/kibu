package work.lclpnet.kibu.access.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.ItemBasedSteering;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import work.lclpnet.kibu.access.type.KibuSaddledComponent;

@Mixin(ItemBasedSteering.class)
public class SaddledComponentMixin implements KibuSaddledComponent {

    @Shadow private boolean boosting;

    @Shadow private int boostTime;

    @Shadow @Final private SynchedEntityData entityData;

    @Shadow @Final private EntityDataAccessor<Integer> boostTimeAccessor;

    @Override
    public void kibu$boost(int ticks) {
        this.boosting = true;
        this.boostTime = 0;
        this.entityData.set(this.boostTimeAccessor, ticks);
    }
}
