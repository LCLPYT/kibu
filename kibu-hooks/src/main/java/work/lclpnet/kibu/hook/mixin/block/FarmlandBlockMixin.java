package work.lclpnet.kibu.hook.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.level.BlockModificationHooks;
import work.lclpnet.kibu.hook.level.FarmlandMoistureChangeCallback;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin extends Block {

    public FarmlandBlockMixin(Properties settings) {
        super(settings);
    }

    @Inject(
            method = "fallOn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/FarmlandBlock;turnToDirt(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"
            ),
            cancellable = true
    )
    public void kibu$onTrample(Level world, BlockState state, BlockPos pos, Entity entity, double fallDistance, CallbackInfo ci) {
        if (BlockModificationHooks.TRAMPLE_FARMLAND.invoker().onModify(world, pos, entity)) {
            ci.cancel();

            super.fallOn(world, state, pos, entity, fallDistance);
        }
    }

    @WrapOperation(
            method = "randomTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
            )
    )
    public boolean kibu$onMoistureChange(ServerLevel instance, BlockPos pos, BlockState state, int i, Operation<Boolean> original) {
        int moisture = state.getValue(FarmlandBlock.MOISTURE);

        if (FarmlandMoistureChangeCallback.HOOK.invoker().onMoistureChange(instance, pos, moisture)) {
            return false;
        }

        return original.call(instance, pos, state, i);
    }

    @Inject(
            method = "turnToDirt",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void kibu$setToDirt(Entity sourceEntity, BlockState state, Level level, BlockPos pos, CallbackInfo ci) {
        if (level instanceof ServerLevel serverWorld
                && state.is(Blocks.FARMLAND)
                && FarmlandMoistureChangeCallback.HOOK.invoker().onMoistureChange(serverWorld, pos, -1)) {
            ci.cancel();
        }
    }
}
