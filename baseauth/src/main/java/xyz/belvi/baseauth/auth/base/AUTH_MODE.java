package xyz.belvi.baseauth.auth.base;

/**
 * Created by zone2 on 10/10/17.
 */

public enum AUTH_MODE {
    FIRE_BASE(false),
    NEXMO(false),
    SINCH(true),
    CORE(true);

    AUTH_MODE(boolean supportCall) {
        this.supportCall = supportCall;
    }

    private boolean supportCall;

    public boolean isCallSupported() {
        return this.supportCall;
    }
}
