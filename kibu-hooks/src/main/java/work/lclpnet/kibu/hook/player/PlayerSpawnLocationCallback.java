package work.lclpnet.kibu.hook.player;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
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
        private final ServerPlayer player;
        private final boolean join;
        private ServerLevel world;
        private Vec3 position;
        private float yaw, pitch;
        private boolean dirty = false;

        public LocationData(ServerPlayer player, boolean join, ServerLevel world, Vec3 position, float yaw, float pitch) {
            this.player = player;
            this.join = join;
            this.world = world;
            this.position = position;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public ServerPlayer getPlayer() {
            return player;
        }

        public ServerLevel getWorld() {
            return world;
        }

        public Vec3 getPosition() {
            return position;
        }

        public void setWorld(ServerLevel world) {
            Objects.requireNonNull(world);
            this.world = world;
            this.dirty = true;
        }

        public void setPosition(Vec3 position) {
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
