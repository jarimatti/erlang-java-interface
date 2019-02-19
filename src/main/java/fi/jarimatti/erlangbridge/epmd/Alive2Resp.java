package fi.jarimatti.erlangbridge.epmd;

/**
 * Epmd registration response message.
 */
public class Alive2Resp {

    private final byte result;
    private final int creation;

    @Override
    public String toString() {
        return String.format("Alive2Resp(%d, %d)", result, creation);
    }

    /**
     * Construct a new response message.
     *
     * @param result The result code.
     * @param creation Creation number.
     */
    Alive2Resp(byte result, int creation) {
        this.result = result;
        this.creation = creation;
    }

    /**
     * A response is successful if the result byte is 0.
     *
     * @return True if the response is successful, false otherwise.
     */
    boolean isSuccess() {
        return result == 0;
    }

    /**
     * @return The result byte for further analysis.
     */
    byte getResult() {
        return result;
    }

    /**
     * @return The creation number.
     */
    int getCreation() {
        return creation;
    }
}
