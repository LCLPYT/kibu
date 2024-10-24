package work.lclpnet.kibu.access.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.ExplosionImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(ExplosionImpl.class)
public interface ExplosionImplAccessor {

    @Invoker
    List<BlockPos> invokeGetBlocksToDestroy();

    @Invoker
    void invokeDamageEntities();

    @Invoker
    void invokeDestroyBlocks(List<BlockPos> positions);

    @Invoker
    void invokeCreateFire(List<BlockPos> positions);
}
