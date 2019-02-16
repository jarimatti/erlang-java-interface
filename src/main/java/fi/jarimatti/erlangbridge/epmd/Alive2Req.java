package fi.jarimatti.erlangbridge.epmd;

public class Alive2Req {

    public static final byte ERLANG_NODE = 77;
    public static final byte HIDDEN_NODE = 72;

    private final String name;
    private final int port;
    private final byte nodeType;

    public Alive2Req(String name, int port) {
        this(name, port, HIDDEN_NODE);
    }

    public Alive2Req(String name, int port, byte nodeType) {
        if (nodeType != ERLANG_NODE && nodeType != HIDDEN_NODE) {
            throw new IllegalArgumentException("Node type must be ERLANG_NODE or HIDDEN_NODE");
        }

        this.name = name;
        this.port = port;
        this.nodeType = nodeType;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public byte getNodeType() {
        return nodeType;
    }

    public byte getProtocol() {
        return (byte) 0;
    }

    public byte getHighestVersion() {
        return (byte) 5;
    }

    public byte getLowestVersion() {
        return (byte) 5;
    }
}
