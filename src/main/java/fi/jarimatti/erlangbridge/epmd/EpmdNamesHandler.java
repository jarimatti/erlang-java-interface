package fi.jarimatti.erlangbridge.epmd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EpmdNamesHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private static final Logger LOGGER = LogManager.getLogger();

    private boolean inProgress = false;
    private StringBuilder builder = new StringBuilder();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        if (!inProgress) {
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
        LOGGER.info("Requesting all names from EPMD.");
        final ByteBuf buf = ctx.alloc().buffer(1);
        buf.writeByte(110);
        ctx.writeAndFlush(buf);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("EpmdNamesHandler: Channel inactive.");
        LOGGER.info("Names: {}", builder.toString());
        super.channelInactive(ctx);
    }
}
