package fi.jarimatti.erlangbridge.epmd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

@ChannelHandler.Sharable
class PortPleaseRequestEncoder extends MessageToByteEncoder<PortPlease2Req> {

    private static final byte TAG = 122;

    @Override
    protected void encode(ChannelHandlerContext ctx, PortPlease2Req msg, ByteBuf out) {
        out.writeByte(TAG);
        out.writeCharSequence(msg.getName(), CharsetUtil.UTF_8);
    }
}
