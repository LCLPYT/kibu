package work.lclpnet.kibu.schematic.mixin;

import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.StructureTemplateManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import work.lclpnet.kibu.schematic.type.KibuServerView;
import work.lclpnet.kibu.schematic.vanilla.VanillaStructureFormat;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements KibuServerView {

    @Shadow public abstract StructureTemplateManager getStructureTemplateManager();

    @Shadow public abstract DynamicRegistryManager.Immutable getRegistryManager();

    @Unique
    private final Object vanillaStructureFormatLock = new Object();
    @Unique @Nullable
    private volatile VanillaStructureFormat vanillaStructureFormat = null;

    @Override
    public VanillaStructureFormat kibu$getVanillaStructureFormat() {
        if (vanillaStructureFormat != null) {
            return vanillaStructureFormat;
        }

        synchronized (vanillaStructureFormatLock) {
            if (vanillaStructureFormat == null) {
                var manager = getStructureTemplateManager();
                var registries = getRegistryManager();

                vanillaStructureFormat = new VanillaStructureFormat(manager, registries);
            }
        }

        return vanillaStructureFormat;
    }
}
