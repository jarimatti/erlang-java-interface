package fi.jarimatti.erlangbridge.node.handshake;

public enum DFlags {
    PUBLISHED  (0x1),
    ATOM_CACHE (0x2),
    EXTENDED_REFERENCES (0x4),
    DIST_MONITOR (0x8),
    FUN_TAGS (0x10),
    DIST_MONITOR_NAME (0x20),
    HIDDEN_ATOM_CACHE (0x40),
    NEW_FUN_TAGS (0x80),
    EXTENDED_PIDS_PORTS (0x100),
    EXPORT_PTR_TAG (0x200),
    BIT_BINARIES (0x400),
    NEW_FLOATS (0x800),
    UNICODE_IO (0x1000),
    DIST_HDR_ATOM_CACHE (0x2000),
    SMALL_ATOM_TAGS (0x4000),
    UTF8_ATOMS (0x10000),
    MAP_TAG (0x20000),
    BIG_CREATION (0x40000),
    SEND_SENDER (0x80000),
    BIG_SEQTRACE_LABELS (0x100000),
    EXIT_PAYLOAD (0x400000),
    FRAGMENTS (0x800000),
    HANDSHAKE_23 (0x1000000),
    SPAWN (1L << 32),
    NAME_ME (1L << 33);

    private final long bit;

    DFlags(long bit) {
        this.bit = bit;
    }

    long set(long flags) {
        return flags | bit;
    }

    boolean isSet(long flags) {
        return (flags & bit) != 0;
    }

    static long defaultFlags() {
        return EXTENDED_REFERENCES.bit | EXTENDED_PIDS_PORTS.bit | UTF8_ATOMS.bit | NEW_FUN_TAGS.bit;
    }

}
