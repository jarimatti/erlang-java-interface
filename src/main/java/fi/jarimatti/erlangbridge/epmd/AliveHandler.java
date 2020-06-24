package fi.jarimatti.erlangbridge.epmd;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AliveHandler extends SimpleChannelInboundHandler<Alive2Resp> {

    private static final Logger LOGGER = LogManager.getLogger();

    private ChannelHandlerContext context;
    private Node node;
    private Promise<Alive2Resp> alivePromise;

    public Future<Alive2Resp> aliveFuture() {
        return alivePromise;
    }

    public synchronized Future<Alive2Resp> register(Node node) {
        if (this.node != null) {
            throw new IllegalStateException("register may only be called once");
        }
        this.node = node;
        context.writeAndFlush(new Alive2Req(node));
        return alivePromise;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        alivePromise = ctx.executor().newPromise();
        context = ctx;

        super.handlerAdded(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Alive2Resp msg) {
        LOGGER.debug("Registration response: {}", msg);
        if (!msg.isSuccess()) {
            LOGGER.error("Registration failed.");
            ctx.close();
        }
        alivePromise.setSuccess(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.warn("Unhandled exception, closing channel: {}", cause.getMessage(), cause);
        alivePromise.setFailure(cause);
        ctx.close();
    }

}
