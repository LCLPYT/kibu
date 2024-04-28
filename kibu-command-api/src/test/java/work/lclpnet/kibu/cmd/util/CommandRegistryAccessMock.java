package work.lclpnet.kibu.cmd.util;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.Optional;
import java.util.stream.Stream;

public class CommandRegistryAccessMock implements CommandRegistryAccess {

    @Override
    public Stream<RegistryKey<? extends Registry<?>>> streamAllRegistryKeys() {
        return Stream.empty();
    }

    @Override
    public <T> Optional<RegistryWrapper.Impl<T>> getOptionalWrapper(RegistryKey<? extends Registry<? extends T>> registryRef) {
        return Optional.empty();
    }
}
