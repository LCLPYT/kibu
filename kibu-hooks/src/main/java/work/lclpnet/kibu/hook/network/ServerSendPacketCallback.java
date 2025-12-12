package work.lclpnet.kibu.hook.network;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import work.lclpnet.kibu.hook.Hook;
import work.lclpnet.kibu.hook.HookFactory;
import work.lclpnet.kibu.hook.util.PendingResult;

/**
 * A callback that is invoked, every time a packet is being sent to a player.
 * Listeners can decide if the packet should:
 *   <li>A) be passed as is</li>
 *   <li>B) be modified, or</li>
 *   <li>C) be retained</li>
 *
 * <h3>Modifying packets</h3>
 * Since packets are immutable most of the time, a new instance of the packet has to be created by the listener.
 * Listeners should not modify packets to a different type of packet,
 * e.g. EntityTrackerUpdateS2CPacket should not be modified to be a packet other than EntityTrackerUpdateS2CPacket.
 * <br>
 * Other listeners that are executed afterward will receive the modified packet and can merge apply their modifications as well.
 *
 * <h3>Retaining packets</h3>
 * If a listener decides to retain a packet, all other listeners must agree to retain it as well (i.e. either pass or retain as well).
 * When another listener modifies a retained packet, the modified packet will still be sent and the retain request(s) by the other listener(s) is ignored.
 */
public interface ServerSendPacketCallback {

    Hook<ServerSendPacketCallback> HOOK = HookFactory.createArrayBacked(ServerSendPacketCallback.class, callbacks -> (packet, handler) -> {
        boolean modified = false, retain = false;

        for (ServerSendPacketCallback callback : callbacks) {
            var pending = callback.overridePacket(packet, handler);

            if (pending.isPass()) continue;

            var opt = pending.get();

            if (opt.isEmpty()) {
                retain = true;
            } else {
                modified = true;
                packet = opt.get();
            }
        }

        if (modified) {
            return PendingResult.of(packet);
        }

        if (retain) {
            return PendingResult.empty();
        }

        return PendingResult.pass();
    });

    PendingResult<Packet<?>> overridePacket(Packet<?> packet, ServerCommonPacketListenerImpl handler);
}
