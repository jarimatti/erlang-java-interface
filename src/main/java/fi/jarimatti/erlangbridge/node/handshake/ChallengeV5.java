package fi.jarimatti.erlangbridge.node.handshake;

class ChallengeV5 {

    static final byte TAG = 'n';
    static final short VERSION = 5;

    private final int flags;
    private final int challenge;
    private final String name;

    ChallengeV5(int flags, int challenge, String name) {
        this.flags = flags;
        this.challenge = challenge;
        this.name = name;
    }

    public int getFlags() {
        return flags;
    }

    public int getChallenge() {
        return challenge;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ChallengeV5{" +
                "flags=" + flags +
                ", challenge=" + challenge +
                ", name='" + name + '\'' +
                '}';
    }
}
