package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.ReplaceDisk;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

@Mixin(ReplaceDisk.class)
public class ReplaceDiskMixin {

    @WrapOperation(
            method = "apply",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"
            )
    )
    public boolean kibu$onSetBlock(ServerLevel instance, BlockPos pos, BlockState state, Operation<Boolean> original,
                                   @Local(argsOnly = true) EnchantedItemInUse context) {

        if (WorldPhysicsHooks.REPLACE_DISK_ENCHANTMENT.invoker().onApply(instance, pos, context.owner(), state)) {
            return false;  // cancel modification
        }

        return original.call(instance, pos, state);
    }
}
