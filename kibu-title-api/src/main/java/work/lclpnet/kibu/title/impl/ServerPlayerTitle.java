package work.lclpnet.kibu.title.impl;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.level.ServerPlayer;
import work.lclpnet.kibu.title.Title;

public class ServerPlayerTitle implements Title {

    private final ServerPlayer player;

    public ServerPlayerTitle(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public void times(int in, int stay, int out) {
        var packet = new ClientboundSetTitlesAnimationPacket(in, stay, out);
        player.connection.send(packet);
    }

    @Override
    public void title(Component title) {
        var packet = new ClientboundSetTitleTextPacket(title);
        player.connection.send(packet);
    }

    @Override
    public void subtitle(Component subtitle) {
        var packet = new ClientboundSetSubtitleTextPacket(subtitle);
        player.connection.send(packet);
    }

    @Override
    public void clear(boolean resetTimes) {
        var packet = new ClientboundClearTitlesPacket(resetTimes);
        player.connection.send(packet);
    }
}
