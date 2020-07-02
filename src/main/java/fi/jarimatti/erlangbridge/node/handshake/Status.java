package fi.jarimatti.erlangbridge.node.handshake;

/**
 * Status message without name.
 */
class Status {

    static final byte TAG = 's';

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    static boolean isValidStatus(String status) {
        switch (status) {
            case "ok":
            case "ok_simultaneous":
            case "nok":
            case "not_allowed":
            case "alive":
            case "true":
            case "false":
                return true;
            default:
                return false;
        }
    }

    @Override
    public String toString() {
        return "Status{" +
                "status='" + status + '\'' +
                '}';
    }
}
