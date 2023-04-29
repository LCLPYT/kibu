package work.lclpnet.kibu.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class BlockStateUtils {

    private BlockStateUtils() {}

    @Nonnull
    public static String stringify(BlockState state) {
        Block block = state.getBlock();
        Identifier blockId = Registries.BLOCK.getId(block);

        var builder = new StringBuilder();
        builder.append(blockId);

        var props = state.getEntries();
        if (!props.isEmpty()) {
            boolean firstProp = true;

            builder.append('[');

            for (var entry : props.entrySet()) {
                if (entry == null) continue;

                var prop = entry.getKey();
                String value = nameValue(prop, entry.getValue());

                if (firstProp) {
                    firstProp = false;
                } else {
                    builder.append(",");
                }

                builder.append(prop.getName()).append("=").append(value);
            }

            builder.append(']');
        }

        return builder.toString();
    }

    @Nullable
    public static BlockState parse(String string) {
        String blockPart = string;
        String propertiesPart = null;

        int propertiesStart = string.indexOf('[');

        if (propertiesStart != -1) {
            blockPart = string.substring(0, propertiesStart);

            int propertiesEnd = string.indexOf(']');
            if (propertiesEnd <= propertiesStart) return null;

            propertiesPart = string.substring(propertiesStart + 1, propertiesEnd);
        }

        var identifier = new Identifier(blockPart);
        var block = Registries.BLOCK.get(identifier);
        var state = block.getDefaultState();

        if (propertiesPart == null) return state;

        var stateManager = block.getStateManager();
        var properties = propertiesPart.split(",");

        for (var property : properties) {
            var parsed = property.split("=");
            if (parsed.length != 2) continue;

            Property<?> prop = stateManager.getProperty(parsed[0]);
            if (prop == null) continue;

            state = with(state, prop, parsed[1]);
        }

        return state;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> String nameValue(Property<T> property, Comparable<?> value) {
        return property.name((T) value);
    }

    private static <T extends Comparable<T>> net.minecraft.block.BlockState with(net.minecraft.block.BlockState state, Property<T> property, String rawValue) {
        Optional<T> value = property.parse(rawValue);
        if (value.isEmpty()) return state;

        return state.with(property, value.get());
    }
}
