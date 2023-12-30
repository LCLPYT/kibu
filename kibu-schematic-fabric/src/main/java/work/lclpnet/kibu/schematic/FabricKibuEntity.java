package work.lclpnet.kibu.schematic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import work.lclpnet.kibu.jnbt.CompoundTag;
import work.lclpnet.kibu.mc.KibuEntity;
import work.lclpnet.kibu.nbt.FabricNbtConversion;

import java.util.UUID;
import java.util.function.Function;

public class FabricKibuEntity implements KibuEntity {

    private final EntityType<?> type;
    private final Vec3d pos;
    private final NbtCompound nbt;

    public FabricKibuEntity(Entity entity) {
        this(entity.getType(), entity.getPos(), entity.writeNbt(new NbtCompound()));
    }

    public FabricKibuEntity(EntityType<?> type, Vec3d pos, NbtCompound nbt) {
        this.type = type;
        this.pos = pos;
        this.nbt = nbt;
    }

    @Override
    public String getId() {
        Identifier id = EntityType.getId(type);

        if (id == null) throw new IllegalStateException("Entity type not registered");

        return id.toString();
    }

    @Override
    public double getX() {
        return pos.getX();
    }

    @Override
    public double getY() {
        return pos.getY();
    }

    @Override
    public double getZ() {
        return pos.getZ();
    }

    @Override
    public CompoundTag getExtraNbt() {
        return FabricNbtConversion.convert(nbt, CompoundTag.class);
    }

    public EntityType<?> getType() {
        return type;
    }

    public Vec3d getPos() {
        return pos;
    }

    public boolean spawn(ServerWorld world, Vec3d pos) {
        nbt.putString("id", getId());

        NbtList posList = new NbtList();
        posList.add(NbtDouble.of(this.pos.x));
        posList.add(NbtDouble.of(this.pos.y));
        posList.add(NbtDouble.of(this.pos.z));

        nbt.put("Pos", posList);

        Entity entity = EntityType.loadEntityWithPassengers(nbt, world, Function.identity());
        if (entity == null) return false;

        Vec3d rootPos = entity.getPos();

        entity.streamSelfAndPassengers().forEach(e -> {
            Vec3d rel = e.getPos().subtract(rootPos);
            e.setPosition(pos.add(rel));
            e.setUuid(UUID.randomUUID());
        });

        return world.spawnNewEntityAndPassengers(entity);
    }
}
