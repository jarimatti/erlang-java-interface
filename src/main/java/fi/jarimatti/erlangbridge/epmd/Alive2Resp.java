package fi.jarimatti.erlangbridge.epmd;

public class Alive2Resp {

    private final byte result;
    private final int creation;

    @Override
    public String toString() {
        return String.format("Alive2Resp(%d, %d)", result, creation);
    }

    public Alive2Resp(byte result, int creation) {
        this.result = result;
        this.creation = creation;
    }

    public boolean isSuccess() {
        return result == 0;
    }

    public byte getResult() {
        return result;
    }

    public int getCreation() {
        return creation;
    }
}
