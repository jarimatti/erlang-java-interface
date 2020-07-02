package fi.jarimatti.erlangbridge.node.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.util.List;

public class StatusCodec extends ByteToMessageCodec<Status> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    protected void encode(ChannelHandlerContext ctx, Status msg, ByteBuf out) throws Exception {
        final byte[] bytes = msg.getStatus().getBytes(Charset.defaultCharset());
        out.writeByte(Status.TAG)
                .writeInt(bytes.length)
                .writeBytes(bytes);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        final byte tag = in.readByte();
        if (tag != Status.TAG) {
            in.resetReaderIndex();
            LOGGER.debug("Message was not Status ({}), tag was {}", Status.TAG, tag);
            return;
        }

        final String status = in.readCharSequence(in.readableBytes(), Charset.defaultCharset()).toString();

        if (Status.isValidStatus(status)) {
            out.add(new Status(status));
        } else {
            throw new IllegalArgumentException("Status is not legal: " + status);
        }
    }
}
