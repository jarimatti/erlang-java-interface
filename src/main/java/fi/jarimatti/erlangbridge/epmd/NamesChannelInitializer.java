package fi.jarimatti.erlangbridge.epmd;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class NamesChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) {
        ch.pipeline()
                .addLast("lengthPrepender", new EpmdLengthPrepender())
                .addLast("names", new NamesHandler());
    }

}
