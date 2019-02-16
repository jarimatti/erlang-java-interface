package fi.jarimatti.erlangbridge;

import com.ericsson.otp.erlang.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MainJava {

    private static final Logger logger = LogManager.getLogger(MainJava.class);

    public static void main(String[] args) throws IOException, OtpErlangExit, OtpErlangDecodeException {
        logger.info("Erlang Bridge starting.");

        logger.debug("Creating OTP Node jnode@DG5");
        OtpNode node = new OtpNode("jnode1@DG5", "ZDGNMHREFMAUDHYRYYMZ");
        logger.debug("Node created");

        logger.debug("Pinging enode1@DG5");
        if (node.ping("enode1@DG5", 5000)) {
            logger.info("Successfully pinged enode1@DG5");
        } else {
            logger.warn("Could not ping enode1@DG5");
        }
        if (node.ping("enode2@DG5", 5000)) {
            logger.info("Successfully pinged enode2@DG5");
        } else {
            logger.warn("Could not ping enode2@DG5");
        }
        if (node.ping("enode3@DG5", 5000)) {
            logger.info("Successfully pinged enode3@DG5");
        } else {
            logger.warn("Could not ping enode3@DG5");
        }

        OtpMbox mbox = node.createMbox("hello");
        loop(mbox);

        node.close();
        logger.info("Erlang Bridge exit.");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void loop(OtpMbox mbox) throws OtpErlangExit, OtpErlangDecodeException {
        boolean run = true;
        logger.info("Entering receive loop.");
        OtpErlangObject exit = new OtpErlangAtom("stop");
        while (run) {
            OtpErlangObject object = mbox.receive();
            if (object.match(exit, null)) {
                logger.info("mbox exiting");
                run = false;
            } else if (object instanceof OtpErlangTuple) {
                logger.info("mbox got message " + object);
                OtpErlangTuple t = (OtpErlangTuple) object;
                OtpErlangPid sender = (OtpErlangPid) t.elementAt(0);
                OtpErlangBinary nameBin = (OtpErlangBinary) t.elementAt(1);
                String name = new String(nameBin.binaryValue(), StandardCharsets.UTF_8);

                String greeting = "Hello, " + name + "!";

                OtpErlangBinary responseBin = new OtpErlangBinary(greeting.getBytes(StandardCharsets.UTF_8));
                mbox.send(sender, responseBin);
            }

        }
    }
}
