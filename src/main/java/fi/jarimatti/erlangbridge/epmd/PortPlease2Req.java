package fi.jarimatti.erlangbridge.epmd;

/**
 * Data class to request details of another node.
 */
class PortPlease2Req {

    private final String name;

    PortPlease2Req(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}
