package fi.jarimatti.erlangbridge.epmd;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PortPleaseHandler extends SimpleChannelInboundHandler<PortPlease2Response> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final String name;
    private Promise<Node> nodePromise;

    PortPleaseHandler(String name) {
        this.name = name;
    }

    public Future<Node> nodeFuture() {
        return nodePromise;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        nodePromise = ctx.executor().newPromise();
        super.handlerAdded(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PortPlease2Response msg) {
        if (msg.isSuccess()) {
            final PortPlease2ResponseSuccess success = (PortPlease2ResponseSuccess) msg;
            final Node node = success.getNode();
            LOGGER.info("Successfully got node info: {}", node);
            nodePromise.setSuccess(node);
        } else {
            final PortPlease2ResponseError error = (PortPlease2ResponseError) msg;
            LOGGER.warn("Error getting node info: {}", error.getError());
            nodePromise.setFailure(
                    new Exception(
                            String.format("Error %d getting node '%s' information.",
                                    error.getError(),
                                    name)));
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        final PortPlease2Req request = new PortPlease2Req(name);
        ctx.writeAndFlush(request);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("Error reading node '{}' information.", name, cause);
        nodePromise.setFailure(cause);
        ctx.close();
    }
}
