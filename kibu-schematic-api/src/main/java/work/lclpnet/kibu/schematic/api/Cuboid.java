package work.lclpnet.kibu.schematic.api;

import work.lclpnet.kibu.mc.BlockPos;

/**
 * Represents a cuboid.
 */
public interface Cuboid {

    /**
     * The cuboid region has its own coordinate system.
     * To transform the coordinates to world coordinates, simply add the offset to a coordinate.
     * @return Offset to world space.
     */
    BlockPos getOffset();

    int getWidth();

    int getHeight();

    int getLength();
}
