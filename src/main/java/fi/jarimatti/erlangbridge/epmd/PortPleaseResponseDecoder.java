package fi.jarimatti.erlangbridge.epmd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

class PortPleaseResponseDecoder extends ReplayingDecoder<PortPlease2Response> {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final byte TAG = 119;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        final byte tag = in.readByte();
        if (tag != TAG) {
            throw new IllegalArgumentException("PORT_PLEASE2_RESP tag should be 119.");
        }

        final byte result = in.readByte();
        if (result != 0) {
            LOGGER.warn("PORT_PLEASE2_RESP error: {}", result);
            out.add(new PortPlease2ResponseError(result));
        } else {
            final Node node = readNode(in);
            LOGGER.debug("PORT_PLEASE2_RESP success: Node = {}", node);
            out.add(new PortPlease2ResponseSuccess(node));
        }
    }

    private Node readNode(ByteBuf in) {
        final Node.Builder builder = Node.builder()
                .port(in.readUnsignedShort())
                .nodeType(in.readByte())
                .protocol(in.readByte())
                .highestVersion(in.readShort())
                .lowestVersion(in.readShort())
                .name(in.readCharSequence(in.readShort(), CharsetUtil.UTF_8).toString());

        final int extraLength = in.readShort();
        if (extraLength > 0) {
            final byte[] extra = new byte[extraLength];
            in.readBytes(extra);
            builder.extra(extra);
        }

        return builder.build();
    }

}
