package work.lclpnet.kibu.schematic.sponge;

import work.lclpnet.kibu.schematic.api.SchematicFormat;
import work.lclpnet.kibu.schematic.api.SchematicReader;
import work.lclpnet.kibu.schematic.api.SchematicWriter;

/**
 * A trimmed implementation of the <a href="https://github.com/SpongePowered/Schematic-Specification/blob/8e6be2d980d3bd794bc29df5fdca5921129fac5d/versions/schematic-2.md">Sponge schematic format, version 2</a>.
 * For comparison, check the implementation of <a href="https://github.com/EngineHub/WorldEdit/tree/df3f7b2ae66de0cf34215c012e7abe4fc61210fc/worldedit-core/src/main/java/com/sk89q/worldedit/extent/clipboard/io/sponge">WorldEdit Core</a>.
 * This implementation is inspired by worldedit-core.
 */
public class SpongeSchematicV2 implements SchematicFormat {

    public static final int FORMAT_VERSION = 2;
    static final String
            SCHEMATIC = "Schematic",
            VERSION = "Version",
            DATA_VERSION = "DataVersion",
            WIDTH = "Width",
            HEIGHT = "Height",
            LENGTH = "Length",
            OFFSET = "Offset",
            PALETTE_MAX = "PaletteMax",
            PALETTE = "Palette",
            BLOCK_DATA = "BlockData",
            BLOCK_ENTITIES = "BlockEntities",
            BLOCK_ENTITY_ID = "Id",
            BLOCK_ENTITY_POS = "Pos";

    @Override
    public SchematicWriter writer() {
        return new WriterV2();
    }

    @Override
    public SchematicReader reader() {
        return new ReaderV2();
    }
}
