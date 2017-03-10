package com.webcon.untils;

/**
 * Created by Administrator on 16-1-13.
 */
public class BreathConstants {

    // sharedPreference access name
    public static final String SP_NAME="settings-freescale-breathMonitor";

    //-----log cat tabs--------
    public static final String APP_LOG="breath_app";
    public static final String LINE_LOG="line chart";
    public static final String THREAD_INDICATOR="THREAD_INDICATOR";
    public static final String PROGRESS_INDICATOR="PROGRESS";
    public static final String ALARMS_BREATH="ALARMS_BREATH";


    //-----record files--------
    public static final String RECORD_FILE_SUFFIX=".nhBreath";
    public static final String RECORD_FILE_test_SUFFIX=".test";
    public static final String RECORD_FILE_FOLDER="nh_breath_rec1";

    // --- time interval above which a wakeUpAlarm is triggered ---
    public static int minBreathInterval;
    public static final int minBreathIntervalDefault=-1;
    public static final int minBreathInterval_init=50;   // normal guess --> 10sec
    public static final String MIN_BREATH_INTERVAL_KEY="MIN_BREATH_INTERVAL_KEY";
    // --- flux below which a wakeUpAlarm is triggered ---
    public static int minFlux;
    public static final int minFluxDefault=-3;
    public static final int minFlux_init=3500;   // normal guess --> 3500
    public static final String MIN_FLUX_KEY="MIN_FLUX_KEY";


    // observer state
    public static final int OBSERVER_NULL=-234;
    public static final int DATA_SENT_OBSERVER=667;


    //-- breath signal state --
    public static final int SIGNAL_BUFFERING=0017;
    public static final int NORMAL_SIGNAL_QUALITY=19756;
    public static final int LOW_SIGNAL_QUALITY=-27368;
    //-- breath signal state related  --
    public static final int SIGNAL_BUFFERING_TIME=300;  // initial data buffering time : 1 minute
}
