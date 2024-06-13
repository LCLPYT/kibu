package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.effect.entity.ReplaceDiskEnchantmentEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

@Mixin(ReplaceDiskEnchantmentEffect.class)
public class ReplaceDiskEnchantmentEffectMixin {

    @WrapOperation(
            method = "apply",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z"
            )
    )
    public boolean kibu$onSetBlock(ServerWorld instance, BlockPos pos, BlockState state, Operation<Boolean> original,
                                   @Local(argsOnly = true) EnchantmentEffectContext context) {

        if (WorldPhysicsHooks.REPLACE_DISK_ENCHANTMENT.invoker().onApply(instance, pos, context.owner(), state)) {
            return false;  // cancel modification
        }

        return original.call(instance, pos, state);
    }
}
