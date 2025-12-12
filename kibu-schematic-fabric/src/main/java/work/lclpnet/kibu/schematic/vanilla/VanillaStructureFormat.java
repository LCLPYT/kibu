package work.lclpnet.kibu.schematic.vanilla;

import net.minecraft.core.HolderLookup;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import work.lclpnet.kibu.schematic.api.*;
import work.lclpnet.kibu.schematic.type.KibuServerView;

public class VanillaStructureFormat implements SchematicFormat {

    private final StructureTemplateManager manager;
    private final HolderLookup.Provider registries;
    private volatile SchematicDeserializer deserializer = null;
    private volatile SchematicReader reader = null;

    public VanillaStructureFormat(StructureTemplateManager manager, HolderLookup.Provider registries) {
        this.manager = manager;
        this.registries = registries;
    }

    @Override
    public SchematicSerializer serializer() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public SchematicDeserializer deserializer() {
        if (deserializer != null) return deserializer;

        synchronized (this) {
            if (deserializer == null) {
                deserializer = new Deserializer(manager, registries);
            }
        }

        return deserializer;
    }

    @Override
    public SchematicWriter writer() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public SchematicReader reader() {
        if (reader != null) return reader;

        synchronized (this) {
            if (reader == null) {
                reader = new Reader(deserializer());
            }
        }

        return reader;
    }

    public static VanillaStructureFormat get(MinecraftServer server) {
        return ((KibuServerView) server).kibu$getVanillaStructureFormat();
    }
}
