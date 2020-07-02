package fi.jarimatti.erlangbridge.node.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

@ChannelHandler.Sharable
public class SendNameV5Decoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        final byte tag = in.readByte();
        if (tag != SendNameV5.TAG) {
            throw new IllegalArgumentException("Tag must be 'n', was " + tag);
        }

        final short version = in.readShort();
        if (version != SendNameV5.VERSION) {
            throw new IllegalArgumentException("Version must be " + SendNameV5.VERSION + ", was " + version);
        }

        final int flags = in.readInt();
        final String name = in.readCharSequence(in.readableBytes(), Charset.defaultCharset()).toString();

        out.add(SendNameV5.builder()
                .flags(flags)
                .name(name)
                .build());
    }

}
