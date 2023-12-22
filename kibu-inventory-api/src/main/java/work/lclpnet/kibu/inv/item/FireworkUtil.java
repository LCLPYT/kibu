package work.lclpnet.kibu.inv.item;

import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

public class FireworkUtil {

    private FireworkUtil() {}

    public static void setFlight(ItemStack stack, byte flight) {
        FireworkRocketItem.setFlight(stack, flight);
    }

    public static void setExplosions(ItemStack stack, FireworkExplosion... explosions) {
        NbtCompound fireworks = stack.getOrCreateSubNbt(FireworkRocketItem.FIREWORKS_KEY);

        NbtList explosionsNbt = new NbtList();

        for (FireworkExplosion explosion : explosions) {
            NbtCompound nbt = new NbtCompound();
            explosion.toNbt(nbt);

            explosionsNbt.add(nbt);
        }

        fireworks.put(FireworkRocketItem.EXPLOSIONS_KEY, explosionsNbt);
    }
}
