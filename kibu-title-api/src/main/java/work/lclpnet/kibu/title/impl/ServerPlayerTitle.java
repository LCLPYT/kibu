package work.lclpnet.kibu.title.impl;

import net.minecraft.network.packet.s2c.play.ClearTitleS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import work.lclpnet.kibu.title.Title;

public class ServerPlayerTitle implements Title {

    private final ServerPlayerEntity player;

    public ServerPlayerTitle(ServerPlayerEntity player) {
        this.player = player;
    }

    @Override
    public void times(int in, int stay, int out) {
        var packet = new TitleFadeS2CPacket(in, stay, out);
        player.networkHandler.sendPacket(packet);
    }

    @Override
    public void title(Text title) {
        var packet = new TitleS2CPacket(title);
        player.networkHandler.sendPacket(packet);
    }

    @Override
    public void subtitle(Text subtitle) {
        var packet = new SubtitleS2CPacket(subtitle);
        player.networkHandler.sendPacket(packet);
    }

    @Override
    public void clear(boolean resetTimes) {
        var packet = new ClearTitleS2CPacket(resetTimes);
        player.networkHandler.sendPacket(packet);
    }
}
