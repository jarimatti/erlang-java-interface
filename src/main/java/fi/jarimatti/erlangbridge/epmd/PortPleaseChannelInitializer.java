package fi.jarimatti.erlangbridge.epmd;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class PortPleaseChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final String name;

    public PortPleaseChannelInitializer(String name) {
        this.name = name;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast(new EpmdLengthPrepender())
                .addLast(new PortPleaseRequestEncoder())
                .addLast(new PortPleaseResponseDecoder())
                .addLast(new PortPleaseHandler(name));
    }

}
