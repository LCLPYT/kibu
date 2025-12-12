package work.lclpnet.kibu.networking.protocol;

import net.minecraft.resources.ResourceLocation;

import java.util.function.IntPredicate;

/**
 * Protocol information
 * @param id        An identifier for the protocol. Will be used as channel name when testing for protocol support in the login process.
 * @param version   The local version of the protocol. Will be sent to peer(s) in the login process.
 * @param supported A predicate that determines if a version is supported
 */
public record Protocol(ResourceLocation id, int version, IntPredicate supported) {

    public Protocol(ResourceLocation id, int version) {
        this(id, version, peer -> peer == version);
    }
}
