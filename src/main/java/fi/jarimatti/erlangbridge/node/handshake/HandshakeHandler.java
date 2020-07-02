package fi.jarimatti.erlangbridge.node.handshake;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class HandshakeHandler extends ChannelDuplexHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    private final String nodeName;
    private final Challenger challenger;

    private int sentChallenge = 0;

    HandshakeHandler(String nodeName, Challenger challenger) {
        this.nodeName = nodeName;
        this.challenger = challenger;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("Channel active");

        super.channelActive(ctx);

        final SendNameV5 sendName = SendNameV5.builder()
                .flags((int) DFlags.defaultFlags())
                .name(nodeName)
                .build();

        ctx.writeAndFlush(sendName);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("Channel inactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOGGER.debug("Got message {}", msg.toString());
        if (msg instanceof Status) {
            final Status status = (Status) msg;
            LOGGER.debug("status response {}", status.getStatus());
            if (!"ok".equals(status.getStatus())) {
                LOGGER.error("Unhandled status {}", status.getStatus());
                throw new IllegalArgumentException("Unsupported status: " + status.getStatus());
            }
        } else if (msg instanceof ChallengeV5) {
            // We got a challenge. Calculate digest and reply.
            final ChallengeV5 challengeMsg = (ChallengeV5) msg;
            LOGGER.debug("Got challenge from {}", challengeMsg.getName());
            final ChallengeReply reply = handleChallenge(challengeMsg);
            ctx.writeAndFlush(reply);
        } else if (msg instanceof ChallengeAck) {
            // Another node has accepted the challenge and now sends it's digest back to us.
            final ChallengeAck challengeAck = (ChallengeAck) msg;
            final boolean isValid = verifyAck(challengeAck);
            if (isValid) {
                LOGGER.debug("Challenge was valid.");
                // TODO: Swap the pipeline to normal messaging here. Handshake has been successful!
            } else {
                LOGGER.error("Challenge was invalid. Closing connection.");
                ctx.close();
            }
        } else {
            final String message = "Unsupported message class: " + msg.getClass().getName();
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    private ChallengeReply handleChallenge(ChallengeV5 msg) throws NoSuchAlgorithmException {
        final byte[] digest = challenger.digest(msg.getChallenge());
        sentChallenge = challenger.newChallenge();
        return new ChallengeReply(sentChallenge, digest);
    }

    private boolean verifyAck(ChallengeAck msg) throws NoSuchAlgorithmException {
        return challenger.verify(sentChallenge, msg.getDigest());
    }
}
