package fi.jarimatti.erlangbridge.node.handshake;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ChallengeAckEncoder extends MessageToByteEncoder<ChallengeAck> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    protected void encode(ChannelHandlerContext ctx, ChallengeAck msg, ByteBuf out) throws Exception {
        out.writeByte(ChallengeAck.TAG)
                .writeBytes(msg.getDigest());
    }

}
