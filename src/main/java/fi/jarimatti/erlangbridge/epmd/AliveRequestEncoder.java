package fi.jarimatti.erlangbridge.epmd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * Encode a node registration request (ALIVE2_REQ) to bytes.
 */
@ChannelHandler.Sharable
class AliveRequestEncoder extends MessageToByteEncoder<Alive2Req> {

    private static final byte TAG = 120;

    @Override
    protected void encode(ChannelHandlerContext ctx, Alive2Req msg, ByteBuf out) {
        final Node node = msg.getNode();
        final ByteBuf name = ctx.alloc().buffer(node.getName().length());
        final int nameLength = name.writeCharSequence(node.getName(), CharsetUtil.UTF_8);
        final int extraFieldLength = 0;
        out
                .writeByte(TAG)
                .writeShort(node.getPort())
                .writeByte(node.getNodeType())
                .writeByte(node.getProtocol())
                .writeShort(node.getHighestVersion())
                .writeShort(node.getLowestVersion())
                .writeShort(nameLength)
                .writeBytes(name)
                .writeShort(extraFieldLength)
                .writeBytes(node.getExtra());
        ReferenceCountUtil.release(name);
    }
}
