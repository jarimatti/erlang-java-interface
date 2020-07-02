package fi.jarimatti.erlangbridge.node.handshake;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

public class Challenger {

    private final byte[] cookie;
    private final Random random = new Random();

    public Challenger(String cookie) {
        this.cookie = cookie.getBytes(Charset.defaultCharset());
    }

    public int newChallenge() {
        return random.nextInt();
    }

    public byte[] digest(int challenge) throws NoSuchAlgorithmException {

        long ch2 = 0;
        if (challenge < 0) {
            ch2 = 1L << 31;
            ch2 |= challenge & 0x7FFFFFFF;
        } else {
            ch2 = challenge;
        }

        final byte[] challengeString = Long.toString(ch2).getBytes(Charset.defaultCharset());
        final MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(cookie);
        return md5.digest(challengeString);
    }

//    protected byte[] genDigest(final int challenge, final String cookie) {
//        int i;
//        long ch2;
//
//        if (challenge < 0) {
//            ch2 = 1L << 31;
//            ch2 |= challenge & 0x7FFFFFFF;
//        } else {
//            ch2 = challenge;
//        }
//        final OtpMD5 context = new OtpMD5();
//        context.update(cookie);
//        context.update("" + ch2);
//
//        final int[] tmp = context.final_bytes();
//        final byte[] res = new byte[tmp.length];
//        for (i = 0; i < tmp.length; ++i) {
//            res[i] = (byte) (tmp[i] & 0xFF);
//        }
//        return res;
//    }

    public boolean verify(int challenge, byte[] digest) throws NoSuchAlgorithmException {
        return Arrays.equals(digest(challenge), digest);
    }
}
