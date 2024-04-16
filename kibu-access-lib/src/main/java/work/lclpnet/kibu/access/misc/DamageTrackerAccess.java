package work.lclpnet.kibu.access.misc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageRecord;
import work.lclpnet.kibu.access.mixin.DamageTrackerAccessor;

import java.util.List;

public class DamageTrackerAccess {

    private DamageTrackerAccess() {}

    public static List<DamageRecord> getRecentDamage(LivingEntity entity) {
        return ((DamageTrackerAccessor) entity.getDamageTracker()).getRecentDamage();
    }
}
