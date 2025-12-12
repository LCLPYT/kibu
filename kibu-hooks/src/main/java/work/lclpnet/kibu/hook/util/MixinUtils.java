package work.lclpnet.kibu.hook.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import work.lclpnet.kibu.hook.entity.EntityDropItemCallback;
import work.lclpnet.kibu.hook.world.WorldPhysicsHooks;

public class MixinUtils {

    private MixinUtils() {}

    public static boolean wrapBlockItemDrop(Level world, Entity entity, Operation<Boolean> original, BlockPos pos) {
        if (entity instanceof ItemEntity itemEntity) {
            ItemStack stack = itemEntity.getItem();

            if (WorldPhysicsHooks.BLOCK_ITEM_DROP.invoker().onTileDrop(world, pos, stack)) {
                // cancelled, do not call original
                return false;
            }
        }

        return original.call(world, entity);
    }

    public static boolean wrapBlockEntityItemDrop(Level world, Entity entity, Operation<Boolean> original, Object mixin) {
        if (mixin instanceof BlockEntity self) {
            return wrapBlockItemDrop(world, entity, original, self.getBlockPos());
        }

        return original.call(world, entity);
    }

    public static boolean wrapEntityItemDrop(Level world, Entity entity, Operation<Boolean> original, Object mixin) {
        if (!(mixin instanceof Entity self) || !(entity instanceof ItemEntity itemEntity))
            return original.call(world, entity);

        if (EntityDropItemCallback.HOOK.invoker().onDropItem(world, self, itemEntity)) {
            // cancelled, do not call original
            return false;
        }

        return original.call(world, entity);
    }
}
