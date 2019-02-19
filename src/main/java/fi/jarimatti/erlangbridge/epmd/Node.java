package fi.jarimatti.erlangbridge.epmd;

import java.util.Arrays;

/**
 * Represents Erlang node information stored in the Epmd.
 */
public class Node {

    /** Erlang node type. */
    private static final byte ERLANG_NODE = 77;

    /** Hidden node type. This is the default. */
    private static final byte HIDDEN_NODE = 72;

    private final String name;
    private final int port;
    private final byte nodeType;
    private final byte protocol;
    private final int highestVersion;
    private final int lowestVersion;
    private final byte[] extra;

    private Node(Builder builder) {
        this.name = builder.name;
        this.port = builder.port;
        this.nodeType = builder.nodeType;
        this.protocol = builder.protocol;
        this.highestVersion = builder.highestVersion;
        this.lowestVersion = builder.lowestVersion;
        this.extra = builder.extra;
    }

    /**
     * @return The node name.
     */
    String getName() {
        return name;
    }

    /**
     * @return The node port.
     */
    int getPort() {
        return port;
    }

    /**
     *
     * @return The node type. Either HIDDEN_NODE or ERLANG_NODE.
     */
    byte getNodeType() {
        return nodeType;
    }

    byte getProtocol() {
        return protocol;
    }

    int getHighestVersion() {
        return highestVersion;
    }

    int getLowestVersion() {
        return lowestVersion;
    }

    byte[] getExtra() {
        return extra;
    }

    @Override
    public String toString() {
        return String.format("Node(name='%s', port=%d, type=%d, protocol=%d, highestVersion=%d, lowestVersion=%d)",
                name, port, nodeType, protocol, highestVersion, lowestVersion);
    }

    public static class Builder {

        private String name;
        private int port;
        private byte nodeType = HIDDEN_NODE;
        private byte protocol = 0;
        private int highestVersion = 5;
        private int lowestVersion = 5;
        private byte[] extra = new byte[]{};

        public static Builder newBuilder() {
            return new Builder();
        }

        public Node build() {
            return new Node(this);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder erlangNode() {
            this.nodeType = ERLANG_NODE;
            return this;
        }

        public Builder hiddenNode() {
            this.nodeType = HIDDEN_NODE;
            return this;
        }

        Builder nodeType(byte nodeType) {
            this.nodeType = nodeType;
            return this;
        }

        Builder protocol(byte protocol) {
            this.protocol = protocol;
            return this;
        }

        Builder highestVersion(int highestVersion) {
            this.highestVersion = highestVersion;
            return this;
        }

        Builder lowestVersion(int lowestVersion) {
            this.lowestVersion = lowestVersion;
            return this;
        }

        Builder extra(byte[] extra) {
            this.extra = Arrays.copyOf(extra, extra.length);
            return this;
        }

    }
}
