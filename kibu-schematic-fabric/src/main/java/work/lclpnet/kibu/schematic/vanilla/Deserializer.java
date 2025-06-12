package work.lclpnet.kibu.schematic.vanilla;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.NbtReadView;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.ErrorReporter;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
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
    private final RegistryWrapper.WrapperLookup registries;

    Deserializer(StructureTemplateManager manager, RegistryWrapper.WrapperLookup registries) {
        this.manager = manager;
        this.registries = registries;
    }

    @Override
    public BlockStructure deserialize(CompoundTag tag, BlockStateAdapter _adapter, BlockStructureFactory factory) {
        NbtCompound nbt = FabricNbtConversion.convert(tag, NbtCompound.class);
        StructureTemplate template = manager.createTemplate(nbt);

        Vec3i size = template.getSize();
        var origin = new KibuBlockPos(0, 0, 0);
        int dataVersion = FabricStructureWrapper.getDataVersion();

        BlockStructure struct = factory.create(size.getX(), size.getY(), size.getZ(), origin, dataVersion);

        var accessor = (StructureTemplateAccessor) template;
        var blockInfoLists = accessor.getBlockInfoLists();

        var adapter = FabricBlockStateAdapter.getInstance();

        if (!blockInfoLists.isEmpty()) {
            // blockInfoLists can contain multiple palettes (e.g. ship wreck structure files)
            // this deserializer only chooses the first one
            addBlocks(struct, blockInfoLists.getFirst().getAll(), adapter);
        }

        addEntities(struct, accessor.getEntities());

        return struct;
    }

    private void addBlocks(BlockStructure struct, List<StructureTemplate.StructureBlockInfo> blocks, FabricBlockStateAdapter adapter) {
        for (StructureTemplate.StructureBlockInfo block : blocks) {
            BlockPos pos = block.pos();
            BlockState state = block.state();

            var kibuPos = adapter.adapt(pos);
            var kibuState = adapter.adapt(state);

            struct.setBlockState(kibuPos, kibuState);

            NbtCompound nbt = block.nbt();

            if (nbt == null) continue;

            addBlockEntity(struct, kibuPos, pos, state, nbt);
        }
    }

    private void addBlockEntity(BlockStructure struct, KibuBlockPos kibuPos, BlockPos pos, BlockState state, NbtCompound nbt) {
        if (!state.hasBlockEntity()) return;

        String id = nbt.getString("id").orElse("");

        var type = Registries.BLOCK_ENTITY_TYPE.getOptionalValue(Identifier.of(id))
                .orElse(null);

        if (type == null) return;

        var blockEntity = new FabricKibuBlockEntity(type, pos, nbt);

        struct.setBlockEntity(kibuPos, blockEntity);
    }

    private void addEntities(BlockStructure struct, List<StructureTemplate.StructureEntityInfo> entities) {
        for (StructureTemplate.StructureEntityInfo entity : entities) {
            NbtCompound nbt = entity.nbt;

            if (nbt.contains("TileX") && nbt.contains("TileY") && nbt.contains("TileZ")) {
                nbt.putInt("TileX", entity.blockPos.getX());
                nbt.putInt("TileY", entity.blockPos.getY());
                nbt.putInt("TileZ", entity.blockPos.getZ());
            }

            EntityType<?> type;

            try (var logging = new ErrorReporter.Logging(logger)) {
                var view = NbtReadView.create(logging, registries, nbt);

                type = EntityType.fromData(view).orElse(null);
            }

            if (type == null) continue;

            var kibuEntity = new FabricKibuEntity(type, entity.pos, nbt);
            struct.addEntity(kibuEntity);
        }
    }
}
