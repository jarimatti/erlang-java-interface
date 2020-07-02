package fi.jarimatti.erlangbridge.node.handshake;

import java.util.Arrays;

public class ChallengeReply {

    static final byte TAG = 'r';
    private final static int DIGEST_LENGTH = 16;

    private final int challenge;
    private final byte[] digest;

    ChallengeReply(int challenge, byte[] digest) {
        if (digest.length != DIGEST_LENGTH) {
            throw new IllegalArgumentException("Digest must be 16 bytes but was " + digest.length);
        }

        this.challenge = challenge;
        this.digest = Arrays.copyOf(digest, DIGEST_LENGTH);
    }

    public int getChallenge() {
        return challenge;
    }

    public byte[] getDigest() {
        return Arrays.copyOf(digest, digest.length);
    }
}
