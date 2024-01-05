package work.lclpnet.kibu.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.state.property.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.joml.Vector3f;
import work.lclpnet.kibu.access.entity.DecorationEntityAccess;
import work.lclpnet.kibu.util.math.Matrix3i;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RotationUtil {

    private static final Set<Direction> HORIZONTAL = Set.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

    // inspired by https://github.com/EngineHub/WorldEdit/blob/master/worldedit-core/src/main/java/com/sk89q/worldedit/extent/transform/BlockTransformExtent.java#L137
    public static BlockState rotate(BlockState state, Matrix3i transformation) {
        if (transformation.equals(Matrix3i.IDENTITY)) return state;

        var props = state.getProperties();

        Map<String, String> directionalProps = new HashMap<>();

        for (var prop : props) {
            BlockState newState = rotateProperty(state, transformation, prop);

            if (newState != state) {
                state = newState;
                continue;
            }

            // check for directional properties
            state = checkDirectionalProps(state, transformation, prop, directionalProps);
        }

        state = modifyDirectionalProps(state, directionalProps);

        return state;
    }

    private static BlockState modifyDirectionalProps(BlockState state, Map<String, String> directionalProps) {
        for (String propName : directionalProps.keySet()) {
            String val = directionalProps.get(propName);
            if (val == null) continue;

            var optProp = state.getProperties().stream()
                    .filter(prop -> propName.equals(prop.getName()))
                    .findAny();

            if (optProp.isEmpty()) continue;

            state = BlockStateUtils.with(state, optProp.get(), val);
        }

        return state;
    }

    private static BlockState checkDirectionalProps(BlockState state, Matrix3i transformation, Property<?> prop, Map<String, String> directionalProps) {
        if ((!(prop instanceof BooleanProperty boolProp) || !state.get(boolProp))
            && (!(prop instanceof EnumProperty<?> enumProp) || state.get(enumProp).asString().equals("none"))) {
            return state;
        }

        String name = prop.getName();

        var optDir = HORIZONTAL.stream()
                .filter(d -> name.equals(d.asString()))
                .findAny();

        if (optDir.isEmpty()) return state;

        Vec3i vec = optDir.get().getVector();
        vec = transformation.transform(vec);

        Direction dir = Direction.fromVector(vec.getX(), vec.getY(), vec.getZ());
        if (dir == null) return state;

        if (prop instanceof BooleanProperty boolProp) {
            state = state.with(boolProp, false);
            directionalProps.put(dir.asString(), "true");
        } else {
            EnumProperty<?> enumProp = (EnumProperty<?>) prop;

            directionalProps.put(dir.asString(), state.get(enumProp).asString());

            // check if there is a value "none"
            if (prop.stream().map(v -> v.property().name(v.value())).anyMatch("none"::equals)) {
                state = BlockStateUtils.with(state, enumProp, "none");
            }
        }

        return state;
    }

    private static BlockState rotateProperty(BlockState state, Matrix3i transformation, Property<?> prop) {
        if (prop instanceof DirectionProperty dirProp) {
            return rotateDirectionProperty(state, transformation, dirProp);
        }

        if (prop instanceof EnumProperty<?> enumProp) {
            return rotateEnumProperty(state, transformation, enumProp);
        }

        if (prop instanceof IntProperty intProp) {
            return rotateIntProperty(state, transformation, intProp);
        }

        return state;
    }

    private static BlockState rotateDirectionProperty(BlockState state, Matrix3i transformation, DirectionProperty prop) {
        Direction dir = state.get(prop);
        BlockPos vec = transformation.transform(dir.getVector());

        Direction rotDir = Direction.fromVector(vec.getX(), vec.getY(), vec.getZ());

        if (rotDir == null || !prop.getValues().contains(rotDir)) return state;

        return state.with(prop, rotDir);
    }

    private static BlockState rotateIntProperty(BlockState state, Matrix3i transformation, IntProperty prop) {
        if (!"rotation".equals(prop.getName())) return state;

        int precision = prop.getValues().size();
        Vector3f vec = getVector(state.get(prop), precision);
        transformation.transform(vec, vec);

        int rotation = getRotation(vec, precision);
        if (!prop.getValues().contains(rotation)) return state;

        return state.with(prop, rotation);
    }

    private static BlockState rotateEnumProperty(BlockState state, Matrix3i transformation, EnumProperty<?> prop) {
        String name = prop.getName();

        if ("axis".equals(name)) {
            return rotateAxisEnum(state, transformation, prop);
        }

        if ("type".equals(name)) {
            return rotateTypeEnum(state, transformation, prop);
        }

        if ("half".equals(name)) {
            return rotateHalfEnum(state, transformation, prop);
        }

        if ("shape".equals(name)) {
            return rotateShapeEnum(state, transformation, prop);
        }

        return state;
    }

    private static BlockState rotateShapeEnum(BlockState state, Matrix3i transformation, EnumProperty<?> prop) {
        if (!transformation.isHorizontalFlip()) return state;

        String val = state.get(prop).asString();

        return BlockStateUtils.with(state, prop, switch (val) {
            case "outer_left" -> "outer_right";
            case "outer_right" -> "outer_left";
            case "inner_left" -> "inner_right";
            case "inner_right" -> "inner_left";
            default -> val;
        });
    }

    private static BlockState rotateHalfEnum(BlockState state, Matrix3i transformation, EnumProperty<?> prop) {
        if (!transformation.isVerticalFlip()) return state;

        String val = state.get(prop).asString();

        return BlockStateUtils.with(state, prop, switch (val) {
            case "bottom" -> "top";
            case "top" -> "bottom";
            default -> val;
        });
    }

    private static BlockState rotateTypeEnum(BlockState state, Matrix3i transformation, EnumProperty<?> prop) {
        // chests
        if (transformation.isHorizontalFlip()) {
            String val = state.get(prop).asString();

            String newVal = switch (val) {
                case "left" -> "right";
                case "right" -> "left";
                default -> null;
            };

            if (newVal != null) {
                return BlockStateUtils.with(state, prop, newVal);
            }
        }

        // slabs
        if (transformation.isVerticalFlip()) {
            String val = state.get(prop).asString();

            String newVal = switch (val) {
                case "bottom" -> "top";
                case "top" -> "bottom";
                default -> null;
            };

            if (newVal != null) {
                return BlockStateUtils.with(state, prop, newVal);
            }
        }

        return state;
    }

    private static BlockState rotateAxisEnum(BlockState state, Matrix3i transformation, EnumProperty<?> prop) {
        Direction dir = switch (state.get(prop).asString()) {
            case "x" -> Direction.EAST;
            case "y" -> Direction.UP;
            case "z" -> Direction.NORTH;
            default -> null;
        };

        if (dir == null) return state;

        Vec3i vec = dir.getVector();
        vec = transformation.transform(vec);

        dir = Direction.fromVector(vec.getX(), vec.getY(), vec.getZ());

        if (dir == null) return state;

        return BlockStateUtils.with(state, prop, dir.getAxis().asString());
    }

    public static Vector3f getVector(int rotation, int precision) {
        Vector3f vec = Direction.NORTH.getUnitVector();
        vec.rotateY((float) Math.PI * -2f / precision * rotation);

        return vec;
    }

    public static int getRotation(Vector3f vec, int precision) {
        vec = vec.normalize(new Vector3f());
        Vector3f north = Direction.NORTH.getUnitVector();

        float angle = vec.angleSigned(north, Direction.UP.getUnitVector());  // angle between [-pi, pi]

        // convert to [0, 2pi]
        float pi2 = 2f * (float) Math.PI;
        angle = (angle + pi2) % (pi2);

        return Math.round(angle * precision / pi2);
    }

    public static void rotateEntity(Entity entity, Matrix3i transformation) {
        if (transformation.equals(Matrix3i.IDENTITY)) return;

        Vec3d rotationVector = entity.getRotationVector();
        rotationVector = transformation.transform(rotationVector);

        double pitch = Math.asin(rotationVector.getY() / rotationVector.length());
        double yaw = Math.atan2(-rotationVector.getX(), rotationVector.getZ());

        entity.setYaw((float) Math.toDegrees(yaw));
        entity.setPitch((float) Math.toDegrees(pitch));

        if (entity instanceof AbstractDecorationEntity deco) {
            Direction facing = deco.getHorizontalFacing();
            Vec3i vec = transformation.transform(facing.getVector());
            facing = Direction.fromVector(vec.getX(), vec.getY(), vec.getZ());

            if (facing != null) {
                DecorationEntityAccess.setFacing(deco, facing);
            }
        }
    }
}
