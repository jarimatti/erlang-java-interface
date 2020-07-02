package fi.jarimatti.erlangbridge.node.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

@ChannelHandler.Sharable
public class SendNameV5Encoder extends MessageToByteEncoder<SendNameV5> {

    @Override
    protected void encode(ChannelHandlerContext ctx, SendNameV5 msg, ByteBuf out) throws Exception {
        out.writeByte(SendNameV5.TAG)
                .writeShort(SendNameV5.VERSION)
                .writeInt(msg.getFlags())
                .writeCharSequence(msg.getName(), Charset.defaultCharset());
    }

}
