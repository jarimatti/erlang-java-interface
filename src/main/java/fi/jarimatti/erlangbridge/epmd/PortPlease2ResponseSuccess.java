package fi.jarimatti.erlangbridge.epmd;

class PortPlease2ResponseSuccess implements PortPlease2Response {

    private final Node node;

    PortPlease2ResponseSuccess(final Node node) {
        this.node = node;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    public Node getNode() {
        return node;
    }
}
