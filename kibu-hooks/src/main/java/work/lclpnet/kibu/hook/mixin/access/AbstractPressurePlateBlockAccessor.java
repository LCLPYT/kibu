package work.lclpnet.kibu.hook.mixin.access;

import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractPressurePlateBlock.class)
public interface AbstractPressurePlateBlockAccessor {

    @Accessor("BOX")
    static Box getBox() {
        throw new AssertionError();
    }
}
