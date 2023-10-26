package work.lclpnet.kibu.hook.player;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;

import java.util.Objects;

public interface PlayerSpawnLocationCallback {

    Hook<PlayerSpawnLocationCallback> HOOK = HookFactory.createArrayBacked(PlayerSpawnLocationCallback.class,
            listeners -> data -> {
                for (var listener : listeners) {
                    listener.onSpawn(data);
                }
            });

    void onSpawn(LocationData data);

    class LocationData {
        private final ServerPlayerEntity player;
        private final boolean join;
        private ServerWorld world;
        private Vec3d position;
        private float yaw, pitch;
        private boolean dirty = false;

        public LocationData(ServerPlayerEntity player, boolean join, ServerWorld world, Vec3d position, float yaw, float pitch) {
            this.player = player;
            this.join = join;
            this.world = world;
            this.position = position;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public ServerPlayerEntity getPlayer() {
            return player;
        }

        public ServerWorld getWorld() {
            return world;
        }

        public Vec3d getPosition() {
            return position;
        }

        public void setWorld(ServerWorld world) {
            Objects.requireNonNull(world);
            this.world = world;
            this.dirty = true;
        }

        public void setPosition(Vec3d position) {
            Objects.requireNonNull(position);
            this.position = position;
            this.dirty = true;
        }

        public boolean isJoin() {
            return join;
        }

        public float getYaw() {
            return yaw;
        }

        public float getPitch() {
            return pitch;
        }

        public void setYaw(float yaw) {
            if (Float.isInfinite(yaw) || Float.isNaN(yaw)) {
                throw new IllegalArgumentException("Invalid yaw");
            }

            this.yaw = yaw;
            this.dirty = true;
        }

        public void setPitch(float pitch) {
            if (Float.isInfinite(pitch) || Float.isNaN(pitch)) {
                throw new IllegalArgumentException("Invalid pitch");
            }

            this.pitch = pitch;
            this.dirty = true;
        }

        public boolean isDirty() {
            return dirty;
        }
    }
}
