package work.lclpnet.kibu.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.lclpnet.kibu.schematic.FabricBlockStateAdapter;
import work.lclpnet.kibu.schematic.FabricKibuEntity;
import work.lclpnet.kibu.structure.BlockStructure;
import work.lclpnet.kibu.util.math.CardinalRotation;
import work.lclpnet.kibu.util.math.Matrix3i;

import java.util.EnumSet;

public class StructureWriter {

    private static final Logger logger = LoggerFactory.getLogger(StructureWriter.class);

    public static void placeStructure(BlockStructure structure, ServerLevel world, Vec3i pos) {
        placeStructure(structure, world, pos, Matrix3i.IDENTITY);
    }

    public static void placeStructure(BlockStructure structure, ServerLevel world, Vec3i pos, CardinalRotation rotation) {
        placeStructure(structure, world, pos, rotation.asMatrix3());
    }

    public static void placeStructure(BlockStructure structure, ServerLevel world, Vec3i pos, @NotNull Matrix3i transformation) {
        placeStructure(structure, world, pos, transformation, EnumSet.noneOf(Option.class));
    }

    public static void placeStructure(BlockStructure structure, ServerLevel world, Vec3i pos,
                                      @NotNull Matrix3i transformation, EnumSet<Option> options) {
        var origin = structure.getOrigin();

        final int ox = origin.getX(), oy = origin.getY(), oz = origin.getZ();
        final int px = pos.getX(), py = pos.getY(), pz = pos.getZ();

        var adapter = FabricBlockStateAdapter.getInstance();
        BlockState air = Blocks.AIR.defaultBlockState();

        final boolean skipBlockEntities = options.contains(Option.SKIP_BLOCK_ENTITIES);
        final boolean skipAir = options.contains(Option.SKIP_AIR);

        int flags = 0;
        if (!options.contains(Option.SKIP_PLAYER_SYNC)) flags |= Block.UPDATE_CLIENTS;
        if (!options.contains(Option.SKIP_NEIGHBOUR_UPDATE)) flags |= Block.UPDATE_NEIGHBORS;
        if (options.contains(Option.FORCE_STATE)) flags |= Block.UPDATE_KNOWN_SHAPE;
        if (options.contains(Option.SKIP_DROPS)) flags |= Block.UPDATE_SUPPRESS_DROPS;

        BlockPos.MutableBlockPos printPos = new BlockPos.MutableBlockPos();

        for (var kibuPos : structure.getBlockPositions()) {
            var kibuState = structure.getBlockState(kibuPos);
            if (skipAir && kibuState.isAir()) continue;

            BlockState state = adapter.revert(kibuState);
            if (state == null) state = air;
            if (skipAir && state.isAir()) continue;

            // convert to local position
            int sx = kibuPos.getX() - ox, sy = kibuPos.getY() - oy, sz = kibuPos.getZ() - oz;

            // apply transformations
            transformation.transform(sx, sy, sz, printPos);
            printPos.set(px + printPos.getX(), py + printPos.getY(), pz + printPos.getZ());

            state = RotationUtil.rotate(state, transformation);

            world.setBlock(printPos, state, flags);

            if (skipBlockEntities) continue;

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

        if (!options.contains(Option.SKIP_ENTITIES)) {
            spawnEntities(structure, world, pos, transformation);
        }
    }

    public static void spawnEntities(BlockStructure structure, ServerLevel world, Vec3i pos, @NotNull Matrix3i transformation) {
        var origin = structure.getOrigin();
        Vec3 offset = new Vec3(origin.getX(), origin.getY(), origin.getZ());

        var adapter = FabricBlockStateAdapter.getInstance();

        for (var kibuEntity : structure.getEntities()) {
            adapter.revert(kibuEntity).ifPresentOrElse(entity -> {
                Vec3 entityPos = rotateEntityPosition(pos, transformation, entity, offset);

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

    private static Vec3 rotateEntityPosition(Vec3i pos, @NotNull Matrix3i transformation, FabricKibuEntity entity, Vec3 pivot) {
        BlockPos tilePos = entity.getTilePos();
        Vec3 entityPos;

        if (tilePos != null) {
            entityPos = transformation.transform(
                    tilePos.getX() - pivot.x,
                    tilePos.getY() - pivot.y,
                    tilePos.getZ() - pivot.z);

            return entityPos.add(
                    pos.getX(),
                    pos.getY(),
                    pos.getZ());
        }

        entityPos = transformation.transform(
                    entity.getX() - pivot.x - 0.5,
                    entity.getY() - pivot.y - 0.5,
                    entity.getZ() - pivot.z - 0.5);

        entityPos = entityPos.add(
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5);

        return entityPos;
    }

    public enum Option {
        SKIP_ENTITIES,
        SKIP_BLOCK_ENTITIES,
        SKIP_AIR,
        FORCE_STATE,
        SKIP_PLAYER_SYNC,
        SKIP_DROPS,
        SKIP_NEIGHBOUR_UPDATE,
    }
}
