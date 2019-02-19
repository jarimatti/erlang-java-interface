package fi.jarimatti.erlangbridge.epmd;

class PortPlease2ResponseError implements PortPlease2Response {

    private final byte error;

    PortPlease2ResponseError(byte error) {
        this.error = error;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    public byte getError() {
        return error;
    }

}
