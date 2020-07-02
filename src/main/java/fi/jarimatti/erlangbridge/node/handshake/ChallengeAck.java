package fi.jarimatti.erlangbridge.node.handshake;

import java.util.Arrays;

public class ChallengeAck {

    static final byte TAG = 'a';
    private final static int DIGEST_LENGTH = 16;

    private final byte[] digest;

    ChallengeAck(byte[] digest) {
        if (digest.length != DIGEST_LENGTH) {
            throw new IllegalArgumentException("Digest must be 16 bytes but was " + digest.length);
        }

        this.digest = Arrays.copyOf(digest, DIGEST_LENGTH);
    }

    public byte[] getDigest() {
        return Arrays.copyOf(digest, digest.length);
    }

    @Override
    public String toString() {
        return "ChallengeAck{" +
                "digest=" + Arrays.toString(digest) +
                '}';
    }
}
