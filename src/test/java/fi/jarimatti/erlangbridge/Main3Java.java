package fi.jarimatti.erlangbridge;

import fi.jarimatti.erlangbridge.epmd.*;
import fi.jarimatti.erlangbridge.node.handshake.Challenger;
import fi.jarimatti.erlangbridge.node.handshake.HandshakeChannelInitialiser;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Main3Java {
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
            // 2. Get known Erlang node information:
            //
            LOGGER.info("Getting erlnode details.");

            final String erlnodeName = "erlnode";
            final Node erlNode = remoteNode(erlnodeName, b).sync().get();
            LOGGER.info("Erlang node: {}", erlNode);

            //
            // 3. Connect to remote node and perform the handshake!
            //
            LOGGER.info("Connecting to erlang node: {}", erlNode);

            final ChannelFuture cf = connectTo(name, erlNode, b);

            waitForNewline();

            LOGGER.info("Starting shutdown.");
            //
            // 4. Wait for all channels to close.
            //
            // Note that the alive/register channel stays open and is only closed
            // when a node is unregistered from the EPMD.
            //
            LOGGER.debug("Closing channel to remote node.");
            cf.channel().close().addListener((ChannelFutureListener) future -> LOGGER.debug("Connection to remote node closed."));
            LOGGER.debug("Closing channel to EPMD");
            f.channel().close().addListener((ChannelFutureListener) future -> LOGGER.debug("Connection to EPMD closed."));

            cf.channel().closeFuture().sync();
            f.channel().closeFuture().sync();
        } finally {
            LOGGER.debug("Closing worker group.");
            workerGroup.shutdownGracefully();
        }
        LOGGER.info("Shutdown complete, exiting.");
    }

    private static Future<Node> remoteNode(String name, Bootstrap bootstrap) throws InterruptedException {
        final Bootstrap b = bootstrap.clone().handler(new PortPleaseChannelInitializer(name));
        final ChannelFuture f = b.connect("localhost", EPMD_PORT).sync();
        return f.channel().pipeline().get(PortPleaseHandler.class).nodeFuture();
    }

    private static ChannelFuture connectTo(String myName, Node node, Bootstrap bootstrap) {
        final Challenger challenger = new Challenger("thisisacookie");
        final Bootstrap b = bootstrap.clone().handler(new HandshakeChannelInitialiser(myName, challenger));
        return b.connect("localhost", node.getPort());
    }

    private static void waitForNewline() {
        System.out.println("Press Enter to exit.");
        System.out.flush();
        final Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}
