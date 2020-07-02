package fi.jarimatti.erlangbridge.node.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.util.List;

public class ChallengeReplyCodec extends ByteToMessageCodec<ChallengeReply> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    protected void encode(ChannelHandlerContext ctx, ChallengeReply msg, ByteBuf out) throws Exception {
        out.writeByte(ChallengeReply.TAG)
                .writeInt(msg.getChallenge())
                .writeBytes(msg.getDigest());
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();
        final byte tag = in.readByte();
        if (tag != ChallengeReply.TAG) {
            in.resetReaderIndex();
            LOGGER.debug("Message was not ChallengeReply ({}), tag was {}", ChallengeReply.TAG, tag);
            return;
        }

        LOGGER.trace("Decoding ChallengeReply.");

        final int challenge = in.readInt();
        final byte[] digest = new byte[16];
        in.readBytes(digest);

        if (in.isReadable()) {
            throw new IllegalArgumentException("Digest should have 16 bytes, instead message has " + in.readableBytes());
        }

        out.add(new ChallengeReply(challenge, digest));
    }
}
