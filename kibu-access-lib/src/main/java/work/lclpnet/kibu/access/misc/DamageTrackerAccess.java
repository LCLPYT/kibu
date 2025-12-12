package work.lclpnet.kibu.access.misc;

import net.minecraft.world.damagesource.CombatEntry;
import net.minecraft.world.entity.LivingEntity;
import work.lclpnet.kibu.access.mixin.CombatTrackerAccessor;

import java.util.List;

public class DamageTrackerAccess {

    private DamageTrackerAccess() {}

    public static List<CombatEntry> getRecentDamage(LivingEntity entity) {
        return ((CombatTrackerAccessor) entity.getCombatTracker()).getEntries();
    }
}
