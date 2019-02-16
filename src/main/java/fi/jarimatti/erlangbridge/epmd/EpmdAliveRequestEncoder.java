package fi.jarimatti.erlangbridge.epmd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

public class EpmdAliveRequestEncoder extends MessageToByteEncoder<Alive2Req> {
    private static final byte ALIVE2_REQ_TAG = 120;

    @Override
    protected void encode(ChannelHandlerContext ctx, Alive2Req msg, ByteBuf out) {
        final ByteBuf name = ctx.alloc().buffer(msg.getName().length());
        final int nameLength = name.writeCharSequence(msg.getName(), CharsetUtil.UTF_8);
        final int extraFieldLength = 0;
        out
                .writeByte(ALIVE2_REQ_TAG)
                .writeShort(msg.getPort())
                .writeByte(msg.getNodeType())
                .writeByte(msg.getProtocol())
                .writeShort(msg.getHighestVersion())
                .writeShort(msg.getLowestVersion())
                .writeShort(nameLength)
                .writeBytes(name)
                .writeShort(extraFieldLength);
        ReferenceCountUtil.release(name);
    }
}
