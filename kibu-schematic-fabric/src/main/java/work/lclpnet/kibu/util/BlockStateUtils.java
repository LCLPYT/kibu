package work.lclpnet.kibu.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BlockStateUtils {

    private BlockStateUtils() {}

    @NotNull
    public static String stringify(BlockState state) {
        Block block = state.getBlock();
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(block);

        var builder = new StringBuilder();
        builder.append(blockId);

        var props = state.getValues();
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

        var identifier = Identifier.parse(blockPart);
        var block = BuiltInRegistries.BLOCK.getValue(identifier);
        var state = block.defaultBlockState();

        if (propertiesPart == null) return state;

        var stateManager = block.getStateDefinition();
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
        return property.getName((T) value);
    }

    static <T extends Comparable<T>> BlockState with(BlockState state, Property<T> property, String rawValue) {
        Optional<T> value = property.getValue(rawValue);
        if (value.isEmpty()) return state;

        return state.setValue(property, value.get());
    }
}
