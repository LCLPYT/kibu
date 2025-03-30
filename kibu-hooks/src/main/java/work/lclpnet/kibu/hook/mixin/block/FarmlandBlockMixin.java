package work.lclpnet.kibu.hook.mixin.block;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.kibu.hook.world.BlockModificationHooks;
import work.lclpnet.kibu.hook.world.FarmlandMoistureChangeCallback;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin extends Block {

    public FarmlandBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(
            method = "onLandedUpon",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/FarmlandBlock;setToDirt(Lnet/minecraft/entity/Entity;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"
            ),
            cancellable = true
    )
    public void kibu$onTrample(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci) {
        if (BlockModificationHooks.TRAMPLE_FARMLAND.invoker().onModify(world, pos, entity)) {
            ci.cancel();

            super.onLandedUpon(world, state, pos, entity, fallDistance);
        }
    }

    @WrapOperation(
            method = "randomTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"
            )
    )
    public boolean kibu$onMoistureChange(ServerWorld instance, BlockPos pos, BlockState state, int i, Operation<Boolean> original) {
        int moisture = state.get(FarmlandBlock.MOISTURE);

        if (FarmlandMoistureChangeCallback.HOOK.invoker().onMoistureChange(instance, pos, moisture)) {
            return false;
        }

        return original.call(instance, pos, state, i);
    }

    @Inject(
            method = "setToDirt",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void kibu$setToDirt(Entity entity, BlockState state, World world, BlockPos pos, CallbackInfo ci) {
        if (world instanceof ServerWorld serverWorld
                && state.isOf(Blocks.FARMLAND)
                && FarmlandMoistureChangeCallback.HOOK.invoker().onMoistureChange(serverWorld, pos, -1)) {
            ci.cancel();
        }
    }
}
