package work.lclpnet.kibu.hook.mixin.access;

import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BasePressurePlateBlock.class)
public interface AbstractPressurePlateBlockAccessor {

    @Accessor("TOUCH_AABB")
    static AABB getBox() {
        throw new AssertionError();
    }
}
