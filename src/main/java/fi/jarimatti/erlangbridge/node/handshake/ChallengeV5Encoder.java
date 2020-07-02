package fi.jarimatti.erlangbridge.node.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;

public class ChallengeV5Encoder extends MessageToByteEncoder<ChallengeV5> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    protected void encode(ChannelHandlerContext ctx, ChallengeV5 msg, ByteBuf out) throws Exception {
        out.writeByte(ChallengeV5.TAG)
                .writeShort(ChallengeV5.VERSION)
                .writeInt(msg.getFlags())
                .writeInt(msg.getChallenge())
                .writeCharSequence(msg.getName(), Charset.defaultCharset());
    }

}
