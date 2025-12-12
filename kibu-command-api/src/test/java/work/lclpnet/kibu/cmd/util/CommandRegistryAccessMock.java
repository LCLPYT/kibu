package work.lclpnet.kibu.cmd.util;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlagSet;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.Stream;

public class CommandRegistryAccessMock implements CommandBuildContext {

    @Override
    public @NotNull Stream<ResourceKey<? extends Registry<?>>> listRegistryKeys() {
        return Stream.empty();
    }

    @Override
    public <T> @NotNull Optional<? extends HolderLookup.RegistryLookup<T>> lookup(ResourceKey<? extends Registry<? extends T>> resourceKey) {
        return Optional.empty();
    }

    @Override
    public @NotNull FeatureFlagSet enabledFeatures() {
        return FeatureFlagSet.of();
    }
}
