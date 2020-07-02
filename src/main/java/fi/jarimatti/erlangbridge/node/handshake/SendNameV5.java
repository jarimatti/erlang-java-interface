package fi.jarimatti.erlangbridge.node.handshake;

/**
 * Handshake send_name version 5 message.
 */
class SendNameV5 {

    final static byte TAG = 'n';
    final static short VERSION = 5;

    private final String name;
    private final int flags;

    private SendNameV5(Builder builder) {
        name = builder.name;
        flags = builder.flags;
    }

    public String getName() {
        return name;
    }

    public int getFlags() {
        return flags;
    }

    static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "SendNameV5{" +
                "name='" + name + '\'' +
                ", flags=" + flags +
                '}';
    }

    static class Builder {
        private String name;
        private int flags;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder flags(int flags) {
            this.flags = flags;
            return this;
        }

        public SendNameV5 build() {
            return new SendNameV5(this);
        }
    }
}
