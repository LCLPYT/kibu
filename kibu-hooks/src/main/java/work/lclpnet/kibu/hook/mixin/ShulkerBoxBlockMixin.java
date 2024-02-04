package work.lclpnet.kibu.hook.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import work.lclpnet.kibu.hook.util.MixinUtils;

@Mixin(ShulkerBoxBlock.class)
public class ShulkerBoxBlockMixin {

    @WrapOperation(
            method = "onBreak",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    private boolean kibu$onDropItem(World world, Entity entity, Operation<Boolean> original, @Local(argsOnly = true) BlockPos pos) {
        return MixinUtils.wrapBlockItemDrop(world, entity, original, pos);
    }
}
