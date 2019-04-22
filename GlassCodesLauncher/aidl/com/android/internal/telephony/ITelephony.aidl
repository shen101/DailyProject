package com.android.internal.telephony;

interface ITelephony {

    /**
     * End call if there is a call in progress, otherwise does nothing.
     */
    boolean endCall();

    /**
     * Check if we are in either an active or holding call
     */
    boolean isOffhook();

    /**
     * Check if an incoming phone call is ringing or call waiting.
     */
    boolean isRinging();

    /**
     * Answer the currently-ringing call.
     *
     * If there's already a current active call, that call will be
     * automatically put on hold.  If both lines are currently in use, the
     * current active call will be ended.
     *
     * TODO: this should be a oneway call (especially since it's called
     * directly from the key queue thread).
     */
    void answerRingingCall();

    /**
     * Request to update location information in service state
     */
    void updateServiceLocation();

    /**
     * Allow mobile data connections.
     */
    boolean enableDataConnectivity();

    /**
     * Disallow mobile data connections.
     */
    boolean disableDataConnectivity();

    /**
      * Returns the network type for data transmission
      */
    int getNetworkType();

    /**
     * Return if the current radio is LTE on GSM
     */
    int getLteOnGsmMode();

    /**
     * Place a call to the specified number.
     */
    void call(String callingPackage, String number);
}