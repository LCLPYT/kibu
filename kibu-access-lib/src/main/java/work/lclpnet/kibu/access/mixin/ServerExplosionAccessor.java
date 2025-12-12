package work.lclpnet.kibu.access.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ServerExplosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ServerExplosion.class)
public interface ServerExplosionAccessor {

    @Invoker
    List<BlockPos> invokeCalculateExplodedPositions();

    @Invoker
    void invokeHurtEntities();

    @Invoker
    void invokeInteractWithBlocks(List<BlockPos> positions);

    @Invoker
    void invokeCreateFire(List<BlockPos> positions);
}
