package work.lclpnet.kibu.hook.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

public class MixinUtils {

    private MixinUtils() {}

    public static boolean wrapBlockItemDrop(World world, Entity entity, Operation<Boolean> original, BlockPos pos) {
        if (entity instanceof ItemEntity itemEntity) {
            ItemStack stack = itemEntity.getStack();

            if (WorldPhysicsHooks.BLOCK_ITEM_DROP.invoker().onTileDrop(world, pos, stack)) {
                // cancelled, do not call original
                return false;
            }
        }

        return original.call(world, entity);
    }

    public static boolean wrapBlockEntityItemDrop(World world, Entity entity, Operation<Boolean> original, Object mixin) {
        if (mixin instanceof BlockEntity self) {
            return wrapBlockItemDrop(world, entity, original, self.getPos());
        }

        return original.call(world, entity);
    }
}
