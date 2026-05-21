package work.lclpnet.kibu.access.misc;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.Optional;
import java.util.function.Consumer;

public class CustomNbt {

    private CustomNbt() {}

    public static <T> void set(ItemStack stack, MapCodec<T> mapCodec, T value) {
        set(stack, mapCodec, value,
                component -> stack.set(DataComponents.CUSTOM_DATA, component));
    }

    public static <T> void set(Entity entity, MapCodec<T> mapCodec, T value) {
        set(entity, mapCodec, value,
                component -> entity.setComponent(DataComponents.CUSTOM_DATA, component));
    }

    private static <T> void set(DataComponentGetter components, MapCodec<T> mapCodec, T value, Consumer<CustomData> setter) {
        CustomData component = components.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);

        mapCodec.codec().encode(value, NbtOps.INSTANCE, component.copyTag())
                .ifSuccess(nbt -> setter.accept(CustomData.of((CompoundTag) nbt)));
    }

    public static <T> Optional<T> get(DataComponentGetter components, MapCodec<T> mapCodec) {
        CustomData component = components.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);

        return mapCodec.codec().decode(NbtOps.INSTANCE, component.copyTag())
                .resultOrPartial()
                .map(Pair::getFirst);
    }
}
