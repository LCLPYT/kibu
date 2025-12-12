package work.lclpnet.kibu.access.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FireworkRocketEntity.class)
public interface FireworkRocketEntityAccessor {

    @Invoker
    void invokeExplode(ServerLevel world);
}
