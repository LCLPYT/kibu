package work.lclpnet.kibu.schematic.vanilla;

import com.mojang.datafixers.DataFixer;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.loader.TemplateSource;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.stream.Stream;

public class KibuTemplateSource extends TemplateSource {

    public KibuTemplateSource(DataFixer fixerUpper, HolderGetter<Block> blockLookup) {
        super(fixerUpper, blockLookup);
    }

    @Override
    public @NonNull Optional<StructureTemplate> load(@NonNull Identifier id) {
        return Optional.empty();
    }

    @Override
    public @NonNull Stream<Identifier> list() {
        return Stream.empty();
    }
}
