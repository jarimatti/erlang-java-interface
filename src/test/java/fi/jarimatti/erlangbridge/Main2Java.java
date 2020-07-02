package fi.jarimatti.erlangbridge;

import fi.jarimatti.erlangbridge.epmd.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutionException;

public class Main2Java {
    private static final int EPMD_PORT = 4369;

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            //
            // 1. Register a node.
            //
            LOGGER.info("Register node");

            final String name = "testi@DG5";
            final int port = 64000;
            final Node node = Node.builder()
                    .name(name)
                    .port(port)
                    .hiddenNode()
                    .build();

            final Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new AliveChannelInitializer())
                    .option(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.connect("localhost", EPMD_PORT).sync();

            final Alive2Resp alive2Resp =
                    f.channel().pipeline()
                            .get(AliveHandler.class)
                            .register(node)
                            .sync()
                            .get();

            LOGGER.info("Got Alive2Resp: {}", alive2Resp);

            //
            // 2. Get all registered nodes.
            //
            LOGGER.info("Get names.");

            Bootstrap b2 = b.clone().handler(new NamesChannelInitializer());
            ChannelFuture f2 = b2.connect("localhost", EPMD_PORT).sync();

            f2.channel().pipeline()
                    .get(NamesHandler.class)
                    .namesFuture()
                    .addListener(future -> {
                        if (future.isSuccess()) {
                            LOGGER.info("All registered names: {}", future.get());
                        } else {
                            LOGGER.error("Could not get all registered names.", future.cause());
                        }
                    });

            f2.channel().closeFuture().addListener(future -> LOGGER.info("All names channel closed: {}", future));

            //
            // 3. Get the registered node details.
            //
            LOGGER.info("Getting my details.");

            Bootstrap b3 = b.clone().handler(new PortPleaseChannelInitializer(name));
            ChannelFuture f3 = b3.connect("localhost", EPMD_PORT).sync();

            f3.channel().pipeline()
                    .get(PortPleaseHandler.class)
                    .nodeFuture()
                    .addListener(future -> {
                        if (future.isSuccess()) {
                            LOGGER.info("Node information: {}", future.get());
                        } else {
                            LOGGER.error("Failed to get node information.", future.cause());
                        }
                    });

            f3.channel().closeFuture().addListener(future -> LOGGER.info("Port please channel closed: {}", future));

            //
            // 4. Wait for all channels to close.
            //
            // Note that the alive/register channel stays open and is only closed
            // when a node is unregistered from the EPMD.
            //
            f3.channel().closeFuture().sync();
            f2.channel().closeFuture().sync();
            f.channel().close().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
