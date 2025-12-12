package work.lclpnet.kibu.map.mixin;

import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MapItemSavedData.class)
public interface MapItemSavedDataAccessor {

    @Accessor @Mutable
    void setLocked(boolean locked);
}
