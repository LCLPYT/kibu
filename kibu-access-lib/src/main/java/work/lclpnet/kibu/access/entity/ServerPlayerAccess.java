package work.lclpnet.kibu.access.entity;

import net.minecraft.core.Position;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class ServerPlayerAccess {

    public static void playSoundToPlayer(
            ServerPlayer player, SoundEvent sound, SoundSource source, double x, double y, double z,
            float volume, float pitch
    ) {
        var holder = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound);
        long seed = player.getRandom().nextLong();

        player.connection.send(new ClientboundSoundPacket(holder, source, x, y, z, volume, pitch, seed));
    }

    public static void playSoundToPlayer(
            ServerPlayer player, SoundEvent sound, SoundSource source, Position pos, float volume, float pitch
    ) {
        playSoundToPlayer(player, sound, source, pos.x(), pos.y(), pos.z(), volume, pitch);
    }

    public static void playSoundToPlayer(
            ServerPlayer player, SoundEvent sound, SoundSource source, float volume, float pitch
    ) {
        playSoundToPlayer(player, sound, source, player.position(), volume, pitch);
    }
}
