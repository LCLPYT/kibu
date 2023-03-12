package work.lclpnet.kibu.hook.mixin;

import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import work.lclpnet.kibu.hook.model.CancellableExplosion;

@Mixin(Explosion.class)
public class ExplosionMixin implements CancellableExplosion {

    @Unique
    public boolean cancelled = false;

    @Override
    public void kibu$setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public boolean kibu$isCancelled() {
        return cancelled;
    }
}
