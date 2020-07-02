package fi.jarimatti.erlangbridge.node.handshake;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;

class LengthDecoder extends LengthFieldBasedFrameDecoder {

    /** Length is 2 bytes, so maximum length is 256 bytes plus 2 for the length field. */
    private final static int MAX_FRAME_LENGTH = 256 + 2;

    public LengthDecoder() {
        super(ByteOrder.BIG_ENDIAN, MAX_FRAME_LENGTH,0, 2, 0, 2, true);
    }
}
