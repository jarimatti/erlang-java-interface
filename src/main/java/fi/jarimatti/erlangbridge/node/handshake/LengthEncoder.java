package fi.jarimatti.erlangbridge.node.handshake;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.LengthFieldPrepender;

import java.nio.ByteOrder;

/**
 * Prepend a two byte big endian length to outgoing messages.
 *
 * This is used during the handshake messages between nodes.
 */
@ChannelHandler.Sharable
class LengthEncoder extends LengthFieldPrepender {
    LengthEncoder() {
        super(ByteOrder.BIG_ENDIAN, 2, 0, false);
    }
}
