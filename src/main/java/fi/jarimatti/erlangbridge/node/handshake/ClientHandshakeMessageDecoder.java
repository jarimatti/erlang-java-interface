package fi.jarimatti.erlangbridge.node.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.util.List;

public class ClientHandshakeMessageDecoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        final byte type = in.readByte();

        switch (type) {
            case Status.TAG:
                out.add(decodeStatus(in));
                break;
            case ChallengeV5.TAG:
                out.add(decodeChallengeV5(in));
                break;
            case ChallengeAck.TAG:
                out.add(decodeChallengeAck(in));
                break;
            default:
                LOGGER.error("Invalid message tag {}", type);
                ctx.close();
        }
    }

    private ChallengeAck decodeChallengeAck(ByteBuf in) {
        LOGGER.trace("Decoding ChallengeAck.");

        final byte[] digest = new byte[16];
        in.readBytes(digest);

        if (in.isReadable()) {
            throw new IllegalArgumentException("Digest should have 16 bytes, instead message has " + in.readableBytes());
        }

        return new ChallengeAck(digest);
    }

    private ChallengeV5 decodeChallengeV5(ByteBuf in) {
        LOGGER.trace("Decoding ChallengeV5.");

        final short version = in.readShort();
        if (version != ChallengeV5.VERSION) {
            throw new IllegalArgumentException("Version should be " + ChallengeV5.VERSION + ", but is " + version);
        }

        final int flags = in.readInt();
        final int challenge = in.readInt();
        final String name = in.readCharSequence(in.readableBytes(), Charset.defaultCharset()).toString();

        return new ChallengeV5(flags, challenge, name);
    }

    private Status decodeStatus(ByteBuf in) {
        LOGGER.trace("Decoding Status.");

        final String status = in.readCharSequence(in.readableBytes(), Charset.defaultCharset()).toString();

        if (Status.isValidStatus(status)) {
            return new Status(status);
        } else {
            throw new IllegalArgumentException("Status is not legal: " + status);
        }
    }
}
