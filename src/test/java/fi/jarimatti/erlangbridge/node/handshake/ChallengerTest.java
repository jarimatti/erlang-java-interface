package fi.jarimatti.erlangbridge.node.handshake;

import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class ChallengerTest {

    @Test
    void digest_is_md5_of_challenge_and_cookie() throws NoSuchAlgorithmException {
        final String cookie = "hello_im_cookie";

        final Challenger challenger = new Challenger(cookie);
        final int challenge = challenger.newChallenge();
        long ch2 = 0;
        if (challenge < 0) {
            ch2 = 1L << 31;
            ch2 |= challenge & 0x7FFFFFFF;
        } else {
            ch2 = challenge;
        }
        final byte[] challengeBytes = Long.toString(ch2).getBytes(Charset.defaultCharset());

        final byte[] actual = challenger.digest(challenge);

        final MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(cookie.getBytes(Charset.defaultCharset()));
        final byte[] expected = md.digest(challengeBytes);

        assertArrayEquals(expected, actual, "challenge digest should match");
    }

    @Test
    void verify_with_correct_cookie_and_digest_is_true() throws NoSuchAlgorithmException {
        final String cookie = "another cookie";

        final Challenger challenger = new Challenger(cookie);
        final int challenge = challenger.newChallenge();
        final byte[] digest = challenger.digest(challenge);

        assertTrue(challenger.verify(challenge, digest));
    }

    @Test
    void verify_with_tampered_digest_is_false() throws NoSuchAlgorithmException {
        final String cookie = "another cookie";

        final Challenger challenger = new Challenger(cookie);
        final int challenge = challenger.newChallenge();
        final byte[] digest = challenger.digest(challenge);
        digest[0] = (byte) ~(digest[0]);

        assertFalse(challenger.verify(challenge, digest));
    }

}
