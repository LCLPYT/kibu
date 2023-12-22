package work.lclpnet.kibu.inv.item;

import net.minecraft.item.FireworkRocketItem;
import net.minecraft.nbt.NbtCompound;

public record FireworkExplosion(FireworkRocketItem.Type type, boolean flicker, boolean trail, int[] colors, int[] fadeColors) {

    public static final FireworkExplosion DEFAULT = new FireworkExplosion(FireworkRocketItem.Type.SMALL_BALL,
            false, false, new int[0], new int[0]);

    public FireworkExplosion withType(FireworkRocketItem.Type type) {
        return new FireworkExplosion(type, flicker, trail, colors, fadeColors);
    }

    public FireworkExplosion withFlicker(boolean flicker) {
        return new FireworkExplosion(type, flicker, trail, colors, fadeColors);
    }

    public FireworkExplosion withTrail(boolean trail) {
        return new FireworkExplosion(type, flicker, trail, colors, fadeColors);
    }

    public FireworkExplosion withColors(int[] colors) {
        return new FireworkExplosion(type, flicker, trail, colors, fadeColors);
    }

    public FireworkExplosion withFadeColors(int[] fadeColors) {
        return new FireworkExplosion(type, flicker, trail, colors, fadeColors);
    }

    public void toNbt(NbtCompound nbt) {
        nbt.putInt(FireworkRocketItem.TYPE_KEY, type.getId());

        if (flicker) {
            nbt.putBoolean(FireworkRocketItem.FLICKER_KEY, true);
        }

        if (trail) {
            nbt.putBoolean(FireworkRocketItem.TRAIL_KEY, true);
        }

        if (colors.length > 0) {
            nbt.putIntArray(FireworkRocketItem.COLORS_KEY, colors);
        }

        if (fadeColors.length > 0) {
            nbt.putIntArray(FireworkRocketItem.FADE_COLORS_KEY, fadeColors);
        }
    }

    public static FireworkExplosion fromNbt(NbtCompound nbt) {
        FireworkRocketItem.Type type = FireworkRocketItem.Type.byId(nbt.getInt(FireworkRocketItem.TYPE_KEY));
        boolean flicker = nbt.getBoolean(FireworkRocketItem.FLICKER_KEY);
        boolean trail = nbt.getBoolean(FireworkRocketItem.TRAIL_KEY);
        int[] colors = nbt.getIntArray(FireworkRocketItem.COLORS_KEY);
        int[] fadeColors = nbt.getIntArray(FireworkRocketItem.FADE_COLORS_KEY);

        return new FireworkExplosion(type, flicker, trail, colors, fadeColors);
    }
}
