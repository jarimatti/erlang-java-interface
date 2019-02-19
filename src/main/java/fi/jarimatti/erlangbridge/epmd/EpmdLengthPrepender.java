package fi.jarimatti.erlangbridge.epmd;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.LengthFieldPrepender;

import java.nio.ByteOrder;

/**
 * Prepend a two byte big endian length to outgoing messages.
 */
@ChannelHandler.Sharable
class EpmdLengthPrepender extends LengthFieldPrepender {
    EpmdLengthPrepender() {
        super(ByteOrder.BIG_ENDIAN, 2, 0, false);
    }
}
