package work.lclpnet.kibu.schematic;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.mc.KibuEntity;
import work.lclpnet.kibu.nbt.FabricNbtConversion;
import work.lclpnet.kibu.util.RotationUtil;
import work.lclpnet.kibu.util.math.Matrix3i;

import java.util.UUID;
import java.util.function.Function;

public class FabricKibuEntity implements KibuEntity {

    private static final Logger logger = LoggerFactory.getLogger(FabricKibuEntity.class);

    private final EntityType<?> type;
    private final Vec3 pos;
    private final net.minecraft.nbt.CompoundTag nbt;

    public FabricKibuEntity(Entity entity) {
        this(entity.getType(), entity.position(), createNbt(entity));
    }

    public FabricKibuEntity(EntityType<?> type, Vec3 pos, net.minecraft.nbt.CompoundTag nbt) {
        this.type = type;
        this.pos = pos;
        this.nbt = nbt;
    }

    private static net.minecraft.nbt.CompoundTag createNbt(Entity entity) {
        var registries = entity.level().registryAccess();

        try (ProblemReporter.ScopedCollector logging = new ProblemReporter.ScopedCollector(entity.problemPath(), logger)) {
            TagValueOutput view = TagValueOutput.createWithContext(logging, registries);

            entity.saveWithoutId(view);

            return view.buildResult();
        }
    }

    @Override
    public String getId() {
        ResourceLocation id = EntityType.getKey(type);

        if (id == null) throw new IllegalStateException("Entity type not registered");

        return id.toString();
    }

    @Override
    public double getX() {
        return pos.x();
    }

    @Override
    public double getY() {
        return pos.y();
    }

    @Override
    public double getZ() {
        return pos.z();
    }

    @Override
    public CompoundTag getExtraNbt() {
        return FabricNbtConversion.convert(nbt, CompoundTag.class);
    }

    @Nullable
    public BlockPos getTilePos() {
        var opt = nbt.getInt("TileX");
        if (opt.isEmpty()) return null;
        int tileX = opt.get();

        opt = nbt.getInt("TileY");
        if (opt.isEmpty()) return null;
        int tileY = opt.get();

        opt = nbt.getInt("TileZ");
        if (opt.isEmpty()) return null;
        int tileZ = opt.get();

        return new BlockPos(tileX, tileY, tileZ);
    }

    public EntityType<?> getType() {
        return type;
    }

    public Vec3 getPos() {
        return pos;
    }

    public boolean spawn(ServerLevel world, Vec3 pos, Matrix3i transformation) {
        nbt.putString("id", getId());

        ListTag posList = new ListTag();
        posList.add(DoubleTag.valueOf(this.pos.x));
        posList.add(DoubleTag.valueOf(this.pos.y));
        posList.add(DoubleTag.valueOf(this.pos.z));

        nbt.put("Pos", posList);

        Entity entity = EntityType.loadEntityRecursive(nbt, world, EntitySpawnReason.STRUCTURE, Function.identity());
        if (entity == null) return false;

        Vec3 rootPos = entity.position();

        entity.getSelfAndPassengers().forEach(e -> {
            Vec3 rel = e.position().subtract(rootPos);
            e.setPos(pos.add(rel));
            e.setUUID(UUID.randomUUID());

            RotationUtil.rotateEntity(e, transformation);
        });

        return world.tryAddFreshEntityWithPassengers(entity);
    }
}
