package fi.jarimatti.erlangbridge.epmd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class EpmdAliveResponseDecoder extends ByteToMessageDecoder {

    public final byte ALIVE2_RESP_TAG = 121;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        final byte tag = in.readByte();

        if (tag != ALIVE2_RESP_TAG) {
            throw new IllegalArgumentException("ALIVE2_RESP tag was not 121");
        }
        final byte result = in.readByte();
        final int creation = in.readShort();

        final Alive2Resp response = new Alive2Resp(result, creation);

        out.add(response);
    }
}
