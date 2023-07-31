package work.lclpnet.kibu.cmd.util;

import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

public class CommandRegistryAccessMock implements CommandRegistryAccess {

    @Override
    public <T> RegistryWrapper<T> createWrapper(RegistryKey<? extends Registry<T>> registryRef) {
        return null;
    }
}
