package fi.jarimatti.erlangbridge.epmd;

import io.netty.handler.codec.LengthFieldPrepender;

import java.nio.ByteOrder;

public class EpmdLengthPrepender extends LengthFieldPrepender {
    public EpmdLengthPrepender() {
        super(ByteOrder.BIG_ENDIAN, 2, 0, false);
    }
}
