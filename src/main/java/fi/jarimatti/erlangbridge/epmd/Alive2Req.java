package fi.jarimatti.erlangbridge.epmd;

/**
 * Epmd registration request.
 *
 * This class is used to register a node in the Epmd.
 */
class Alive2Req {

    private final Node node;

    /**
     * Construct a new registration request.
     *
     * @param node The node that will be registered.
     */
    Alive2Req(Node node) {
        this.node = node;
    }

    /**
     * @return The node in this request.
     */
    Node getNode() {
        return node;
    }
}
