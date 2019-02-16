package fi.jarimatti.erlangbridge;

import fi.jarimatti.erlangbridge.epmd.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main2Java {
    public static final int EPMD_PORT = 4369;

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            LOGGER.info("Register Channel");
            EpmdAliveChannelInitializer registerChannelInitializer = new EpmdAliveChannelInitializer(new EpmdRegisterHandler());
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(registerChannelInitializer)
                    .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.connect("localhost", EPMD_PORT).sync();

            Thread.sleep(5000);

            LOGGER.info("Get names");
            Bootstrap b2 = new Bootstrap();
            b2.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast(new EpmdLengthPrepender())
                                    .addLast(new EpmdNamesHandler());
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f2 = b2.connect("localhost", EPMD_PORT).sync();

            f2.channel().closeFuture().sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }
}
