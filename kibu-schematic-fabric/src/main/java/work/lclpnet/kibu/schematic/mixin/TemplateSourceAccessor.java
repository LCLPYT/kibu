package work.lclpnet.kibu.schematic.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.loader.TemplateSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(TemplateSource.class)
public interface TemplateSourceAccessor {

    @Invoker
    StructureTemplate invokeReadStructure(final CompoundTag tag);
}
