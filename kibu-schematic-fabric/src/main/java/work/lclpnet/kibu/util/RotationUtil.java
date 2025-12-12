package work.lclpnet.kibu.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
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

    // for blocks which have properties like east=true,north=none etc. e.g. fences or walls
    private static BlockState checkDirectionalProps(BlockState state, Matrix3i transformation, Property<?> prop, Map<String, String> directionalProps) {
        if ((!(prop instanceof BooleanProperty boolProp) || !state.getValue(boolProp))
            && (!(prop instanceof EnumProperty<?> enumProp) || state.getValue(enumProp).getSerializedName().equals("none"))) {
            return state;
        }

        String name = prop.getName();

        var optDir = HORIZONTAL.stream()
                .filter(d -> name.equals(d.getSerializedName()))
                .findAny();

        if (optDir.isEmpty()) return state;

        Vec3i vec = optDir.get().getUnitVec3i();
        vec = transformation.transform(vec);

        Direction dir = Direction.getNearest(vec.getX(), vec.getY(), vec.getZ(), null);
        if (dir == null) return state;

        if (prop instanceof BooleanProperty boolProp) {
            state = state.setValue(boolProp, false);
            directionalProps.put(dir.getSerializedName(), "true");
        } else {
            EnumProperty<?> enumProp = (EnumProperty<?>) prop;

            directionalProps.put(dir.getSerializedName(), state.getValue(enumProp).getSerializedName());

            // check if there is a value "none"
            if (prop.getAllValues().map(v -> v.property().getName(v.value())).anyMatch("none"::equals)) {
                state = BlockStateUtils.with(state, enumProp, "none");
            }
        }

        return state;
    }

    private static BlockState rotateProperty(BlockState state, Matrix3i transformation, Property<?> prop) {
        if (prop instanceof EnumProperty<?> enumProp) {
            return rotateEnumProperty(state, transformation, enumProp);
        }

        if (prop instanceof IntegerProperty intProp) {
            return rotateIntProperty(state, transformation, intProp);
        }

        return state;
    }

    private static BlockState rotateIntProperty(BlockState state, Matrix3i transformation, IntegerProperty prop) {
        if (!"rotation".equals(prop.getName())) return state;

        int precision = prop.getPossibleValues().size();
        Vector3f vec = getVector(state.getValue(prop), precision);
        transformation.transform(vec, vec);

        int rotation = getRotation(vec, precision);
        if (!prop.getPossibleValues().contains(rotation)) return state;

        return state.setValue(prop, rotation);
    }

    @SuppressWarnings("unchecked")
    private static BlockState rotateEnumProperty(BlockState state, Matrix3i transformation, EnumProperty<?> prop) {
        String name = prop.getName();

        if ("facing".equals(name) && prop.getValueClass() == Direction.class) {
            return rotateDirectionProperty(state, transformation, (EnumProperty<Direction>) prop);
        }

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

        if ("orientation".equals(name) && prop.getValueClass() == FrontAndTop.class) {
            return rotateOrientationEnum(state, transformation, (EnumProperty<FrontAndTop>) prop);
        }

        return state;
    }

    private static BlockState rotateDirectionProperty(BlockState state, Matrix3i transformation, EnumProperty<Direction> prop) {
        Direction dir = state.getValue(prop);
        BlockPos vec = transformation.transform(dir.getUnitVec3i());

        Direction rotDir = Direction.getNearest(vec.getX(), vec.getY(), vec.getZ(), null);

        if (rotDir == null || !prop.getPossibleValues().contains(rotDir)) return state;

        return state.setValue(prop, rotDir);
    }

    private static BlockState rotateOrientationEnum(BlockState state, Matrix3i transformation, EnumProperty<FrontAndTop> prop) {
        FrontAndTop orientation = state.getValue(prop);

        BlockPos vec = transformation.transform(orientation.front().getUnitVec3i());
        Direction facing = Direction.getNearest(vec.getX(), vec.getY(), vec.getZ(), null);

        vec = transformation.transform(orientation.top().getUnitVec3i());
        Direction rotation = Direction.getNearest(vec.getX(), vec.getY(), vec.getZ(), null);

        if (facing == null || rotation == null) {
            return state;
        }

        FrontAndTop rotOrientation = FrontAndTop.fromFrontAndTop(facing, rotation);

        if (rotOrientation == null) {
            return state;
        }

        return state.setValue(prop, rotOrientation);
    }

    private static BlockState rotateShapeEnum(BlockState state, Matrix3i transformation, EnumProperty<?> prop) {
        if (!transformation.isHorizontalFlip()) return state;

        String val = state.getValue(prop).getSerializedName();

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

        String val = state.getValue(prop).getSerializedName();

        return BlockStateUtils.with(state, prop, switch (val) {
            case "bottom" -> "top";
            case "top" -> "bottom";
            default -> val;
        });
    }

    private static BlockState rotateTypeEnum(BlockState state, Matrix3i transformation, EnumProperty<?> prop) {
        // chests
        if (transformation.isHorizontalFlip()) {
            String val = state.getValue(prop).getSerializedName();

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
            String val = state.getValue(prop).getSerializedName();

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
        Direction dir = switch (state.getValue(prop).getSerializedName()) {
            case "x" -> Direction.EAST;
            case "y" -> Direction.UP;
            case "z" -> Direction.NORTH;
            default -> null;
        };

        if (dir == null) return state;

        Vec3i vec = dir.getUnitVec3i();
        vec = transformation.transform(vec);

        dir = Direction.getNearest(vec.getX(), vec.getY(), vec.getZ(), null);

        if (dir == null) return state;

        return BlockStateUtils.with(state, prop, dir.getAxis().getSerializedName());
    }

    public static Vector3f getVector(int rotation, int precision) {
        Vector3f vec = Direction.NORTH.step();
        vec.rotateY((float) Math.PI * -2f / precision * rotation);

        return vec;
    }

    public static int getRotation(Vector3f vec, int precision) {
        vec = vec.normalize(new Vector3f());
        Vector3f north = Direction.NORTH.step();

        float angle = vec.angleSigned(north, Direction.UP.step());  // angle between [-pi, pi]

        // convert to [0, 2pi]
        float pi2 = 2f * (float) Math.PI;
        angle = (angle + pi2) % (pi2);

        return Math.round(angle * precision / pi2);
    }

    public static void rotateEntity(Entity entity, Matrix3i transformation) {
        if (transformation.equals(Matrix3i.IDENTITY)) return;

        Vec3 rotationVector = entity.getLookAngle();
        rotationVector = transformation.transform(rotationVector);

        double pitch = Math.asin(rotationVector.y() / rotationVector.length());
        double yaw = Math.atan2(-rotationVector.x(), rotationVector.z());

        entity.setYRot((float) Math.toDegrees(yaw));
        entity.setXRot((float) Math.toDegrees(pitch));

        if (entity instanceof HangingEntity deco) {
            Direction facing = deco.getDirection();
            Vec3i vec = transformation.transform(facing.getUnitVec3i());
            facing = Direction.getNearest(vec.getX(), vec.getY(), vec.getZ(), null);

            if (facing != null) {
                DecorationEntityAccess.setFacing(deco, facing);
            }
        }
    }
}
