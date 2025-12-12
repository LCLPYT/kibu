package work.lclpnet.kibu.schematic.vanilla;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.TagValueInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.mc.BlockStateAdapter;
import work.lclpnet.kibu.mc.KibuBlockPos;
import work.lclpnet.kibu.nbt.FabricNbtConversion;
import work.lclpnet.kibu.schematic.FabricBlockStateAdapter;
import work.lclpnet.kibu.schematic.FabricKibuBlockEntity;
import work.lclpnet.kibu.schematic.FabricKibuEntity;
import work.lclpnet.kibu.schematic.FabricStructureWrapper;
import work.lclpnet.kibu.schematic.api.BlockStructureFactory;
import work.lclpnet.kibu.schematic.api.SchematicDeserializer;
import work.lclpnet.kibu.schematic.mixin.StructureTemplateAccessor;
import work.lclpnet.kibu.structure.BlockStructure;

import java.util.List;

class Deserializer implements SchematicDeserializer {

    private static final Logger logger = LoggerFactory.getLogger(Deserializer.class);

    private final StructureTemplateManager manager;
    private final HolderLookup.Provider registries;

    Deserializer(StructureTemplateManager manager, HolderLookup.Provider registries) {
        this.manager = manager;
        this.registries = registries;
    }

    @Override
    public BlockStructure deserialize(CompoundTag tag, BlockStateAdapter _adapter, BlockStructureFactory factory) {
        net.minecraft.nbt.CompoundTag nbt = FabricNbtConversion.convert(tag, net.minecraft.nbt.CompoundTag.class);
        StructureTemplate template = manager.readStructure(nbt);

        Vec3i size = template.getSize();
        var origin = new KibuBlockPos(0, 0, 0);
        int dataVersion = FabricStructureWrapper.getDataVersion();

        BlockStructure struct = factory.create(size.getX(), size.getY(), size.getZ(), origin, dataVersion);

        var accessor = (StructureTemplateAccessor) template;
        var blockInfoLists = accessor.getPalettes();

        var adapter = FabricBlockStateAdapter.getInstance();

        if (!blockInfoLists.isEmpty()) {
            // blockInfoLists can contain multiple palettes (e.g. ship wreck structure files)
            // this deserializer only chooses the first one
            addBlocks(struct, blockInfoLists.getFirst().blocks(), adapter);
        }

        addEntities(struct, accessor.getEntityInfoList());

        return struct;
    }

    private void addBlocks(BlockStructure struct, List<StructureTemplate.StructureBlockInfo> blocks, FabricBlockStateAdapter adapter) {
        for (StructureTemplate.StructureBlockInfo block : blocks) {
            BlockPos pos = block.pos();
            BlockState state = block.state();

            var kibuPos = adapter.adapt(pos);
            var kibuState = adapter.adapt(state);

            struct.setBlockState(kibuPos, kibuState);

            net.minecraft.nbt.CompoundTag nbt = block.nbt();

            if (nbt == null) continue;

            addBlockEntity(struct, kibuPos, pos, state, nbt);
        }
    }

    private void addBlockEntity(BlockStructure struct, KibuBlockPos kibuPos, BlockPos pos, BlockState state, net.minecraft.nbt.CompoundTag nbt) {
        if (!state.hasBlockEntity()) return;

        String id = nbt.getString("id").orElse("");

        var type = BuiltInRegistries.BLOCK_ENTITY_TYPE.getOptional(Identifier.parse(id))
                .orElse(null);

        if (type == null) return;

        var blockEntity = new FabricKibuBlockEntity(type, pos, nbt);

        struct.setBlockEntity(kibuPos, blockEntity);
    }

    private void addEntities(BlockStructure struct, List<StructureTemplate.StructureEntityInfo> entities) {
        for (StructureTemplate.StructureEntityInfo entity : entities) {
            net.minecraft.nbt.CompoundTag nbt = entity.nbt;

            if (nbt.contains("TileX") && nbt.contains("TileY") && nbt.contains("TileZ")) {
                nbt.putInt("TileX", entity.blockPos.getX());
                nbt.putInt("TileY", entity.blockPos.getY());
                nbt.putInt("TileZ", entity.blockPos.getZ());
            }

            EntityType<?> type;

            try (var logging = new ProblemReporter.ScopedCollector(logger)) {
                var view = TagValueInput.create(logging, registries, nbt);

                type = EntityType.by(view).orElse(null);
            }

            if (type == null) continue;

            var kibuEntity = new FabricKibuEntity(type, entity.pos, nbt);
            struct.addEntity(kibuEntity);
        }
    }
}
