package work.lclpnet.kibu.schematic.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.DataFixer;
import net.minecraft.core.HolderGetter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldStem;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import work.lclpnet.kibu.schematic.type.KibuServerView;
import work.lclpnet.kibu.schematic.vanilla.VanillaStructureFormat;

import java.util.Objects;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements KibuServerView {

    @Unique @Nullable
    private volatile VanillaStructureFormat vanillaStructureFormat = null;

    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;<init>(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;Lcom/mojang/datafixers/DataFixer;Lnet/minecraft/core/HolderGetter;)V"
            )
    )
    public HolderGetter<Block> kibu$initVanillaStructureFormat(
            HolderGetter<Block> blockLookup,
            @Local(argsOnly = true, name = "worldStem") WorldStem worldStem,
            @Local(argsOnly = true, name = "fixerUpper") DataFixer fixerUpper
    ) {
        var registryAccess = worldStem.registries().compositeAccess();

        this.vanillaStructureFormat = new VanillaStructureFormat(fixerUpper, blockLookup, registryAccess);

        return blockLookup;
    }

    @Override
    public VanillaStructureFormat kibu$getVanillaStructureFormat() {
        return Objects.requireNonNull(vanillaStructureFormat, "Vanilla structure format not initialized");
    }
}
