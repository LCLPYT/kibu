package work.lclpnet.kibu.schematic.api;

import work.lclpnet.kibu.mc.KibuBlockPos;

/**
 * Represents a cuboid.
 */
public interface Cuboid {

    /**
     * The origin of the cuboid. This is also the minimum point.
     * @return Offset to world space; minimum point (origin) of the cuboid.
     */
    KibuBlockPos getOrigin();

    int getWidth();

    int getHeight();

    int getLength();
}
