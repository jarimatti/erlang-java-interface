package fi.jarimatti.erlangbridge.epmd;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class EpmdAliveChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final ChannelHandler handler;

    public EpmdAliveChannelInitializer(ChannelHandler handler) {
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast(new EpmdLengthPrepender())
                .addLast(new EpmdAliveResponseDecoder())
                .addLast(new EpmdAliveRequestEncoder())
                .addLast(handler);
    }
}
