package fi.jarimatti.erlangbridge.epmd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EpmdRegisterHandler extends SimpleChannelInboundHandler<Alive2Resp> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Alive2Resp msg) {
        LOGGER.info("Registration response: {}", msg);
        if (!msg.isSuccess()) {
            LOGGER.error("Registration failed.");
            ctx.close();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LOGGER.info("Channel active, sending register request to EPMD.");

        // TODO: Put as parameter to constructor.
        final int listenPort = 64000;
        final byte nodeType = Alive2Req.ERLANG_NODE;
        final String nodeName = "testnode@DG5";

        final Alive2Req request = new Alive2Req(nodeName, listenPort, nodeType);

        ctx.writeAndFlush(request);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.warn("Unhandled exception, closing channel: {}", cause);
        ctx.close();
    }
}
