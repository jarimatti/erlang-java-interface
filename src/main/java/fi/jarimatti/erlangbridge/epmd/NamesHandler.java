package fi.jarimatti.erlangbridge.epmd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NamesHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger LOGGER = LogManager.getLogger();

    private boolean inProgress = false;
    private final StringBuilder builder = new StringBuilder();

    private Promise<String> namesPromise;

    public Future<String> namesFuture() {
        return namesPromise;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        namesPromise = ctx.executor().newPromise();
        super.handlerAdded(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        if (!inProgress && msg.readableBytes() >= 4) {
            final int port = msg.readInt();
            LOGGER.debug("EPMD port number: {}", port);
            inProgress = true;
        }
        final int readable = msg.readableBytes();
        final String s = msg.readCharSequence(readable, CharsetUtil.UTF_8).toString();

        builder.append(s);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        LOGGER.debug("Requesting all names from EPMD.");
        final ByteBuf buf = ctx.alloc().buffer(1);
        buf.writeByte(110);
        ctx.writeAndFlush(buf);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        final String allNames = builder.toString();
        LOGGER.debug("All active nodes: {}", allNames);

        namesPromise.setSuccess(allNames);

        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("Exception getting names.", cause);
        namesPromise.setFailure(cause);
        ctx.close();
    }
}
