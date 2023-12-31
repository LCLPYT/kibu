package work.lclpnet.kibu.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.lclpnet.kibu.schematic.FabricBlockStateAdapter;
import work.lclpnet.kibu.structure.BlockStructure;
import work.lclpnet.kibu.util.math.CardinalRotation;
import work.lclpnet.kibu.util.math.Matrix3i;

import javax.annotation.Nonnull;
import java.util.EnumSet;

public class StructureWriter {

    private static final Logger logger = LoggerFactory.getLogger(StructureWriter.class);

    public static void placeStructure(BlockStructure structure, ServerWorld world, Vec3i pos) {
        placeStructure(structure, world, pos, Matrix3i.IDENTITY);
    }

    public static void placeStructure(BlockStructure structure, ServerWorld world, Vec3i pos, CardinalRotation rotation) {
        placeStructure(structure, world, pos, rotation.asMatrix3());
    }

    public static void placeStructure(BlockStructure structure, ServerWorld world, Vec3i pos, @Nonnull Matrix3i transformation) {
        placeStructure(structure, world, pos, transformation, EnumSet.allOf(Option.class));
    }

    public static void placeStructure(BlockStructure structure, ServerWorld world, Vec3i pos,
                                      @Nonnull Matrix3i transformation, EnumSet<Option> options) {
        var origin = structure.getOrigin();

        final int ox = origin.getX(), oy = origin.getY(), oz = origin.getZ();
        final int px = pos.getX(), py = pos.getY(), pz = pos.getZ();

        var adapter = FabricBlockStateAdapter.getInstance();
        BlockState air = Blocks.AIR.getDefaultState();

        final boolean readBlockEntities = options.contains(Option.READ_BLOCK_ENTITIES);

        BlockPos.Mutable printPos = new BlockPos.Mutable();

        for (var kibuPos : structure.getBlockPositions()) {
            // convert to local position
            int sx = kibuPos.getX() - ox, sy = kibuPos.getY() - oy, sz = kibuPos.getZ() - oz;

            // apply transformations
            transformation.transform(sx, sy, sz, printPos);
            printPos.set(px + printPos.getX(), py + printPos.getY(), pz + printPos.getZ());

            var kibuState = structure.getBlockState(kibuPos);

            BlockState state = adapter.revert(kibuState);
            if (state == null) state = air;

            state = RotationUtil.rotate(state, transformation);

            world.setBlockState(printPos, state);

            if (!readBlockEntities) continue;

            var kibuBlockEntity = structure.getBlockEntity(kibuPos);
            if (kibuBlockEntity == null) continue;

            adapter.revert(kibuBlockEntity).ifPresentOrElse(blockEntity -> {
                try {
                    if (!blockEntity.spawn(world, printPos)) {
                        logger.warn("Failed to spawn block entity '{}' at {}", kibuBlockEntity.getId(), kibuPos);
                    }
                } catch (Throwable throwable) {
                    logger.error("Failed to create block entity '{}' at {}", kibuBlockEntity.getId(), kibuPos, throwable);
                }
            }, () -> logger.warn("Unknown block entity type '{}'", kibuBlockEntity.getId()));
        }

        if (options.contains(Option.SPAWN_ENTITIES)) {
            spawnEntities(structure, world, pos, transformation);
        }
    }

    public static void spawnEntities(BlockStructure structure, ServerWorld world, Vec3i pos, @Nonnull Matrix3i transformation) {
        var origin = structure.getOrigin();

        final int ox = origin.getX(), oy = origin.getY(), oz = origin.getZ();

        var adapter = FabricBlockStateAdapter.getInstance();

        for (var kibuEntity : structure.getEntities()) {
            adapter.revert(kibuEntity).ifPresentOrElse(entity -> {
                Vec3d entityPos = transformation.transform(entity.getX() - ox, entity.getY() - oy, entity.getZ() - oz);
                entityPos = entityPos.add(pos.getX(), pos.getY(), pos.getZ());

                try {
                    if (!entity.spawn(world, entityPos, transformation)) {
                        logger.warn("Failed to spawn entity '{}' at {}", kibuEntity.getId(), entity.getPos());
                    }
                } catch (Throwable throwable) {
                    logger.warn("Failed to create entity '{}' at {}", kibuEntity.getId(), entity.getPos(), throwable);
                }
            }, () -> logger.warn("Unknown entity type '{}'", kibuEntity.getId()));
        }
    }

    public enum Option {
        SPAWN_ENTITIES,
        READ_BLOCK_ENTITIES
    }
}
