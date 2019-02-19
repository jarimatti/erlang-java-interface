package fi.jarimatti.erlangbridge.epmd;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;

/**
 * Initialize a channel that is used to register a node to Epmd.
 * <p>
 * Erlang distribution protocol documentation talks about ALIVE2_REQ and ALIVE2_RESP,
 * so this class name is AliveChannelInitializer.
 */
public class AliveChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast(new EpmdLengthPrepender())
                .addLast(new AliveRequestEncoder())
                .addLast(new FixedLengthFrameDecoder(4))
                .addLast(new AliveResponseDecoder())
                .addLast(new AliveHandler());
    }

}
