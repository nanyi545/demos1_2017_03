package com.webcon.untils.alarmManagement;

import com.webcon.untils.BreathConstants;

/**
 * Created by Administrator on 16-1-27.
 */
public class AlarmManager {


    /**
     * interface exposed to network api to transmit alarms to the server
     */
    public interface Reactions{
        void onSmallFlux(int[] waveForm,int[] fluxes);  //fluxes[0]:actual, fluxes[1]:threshold
        void onOverTime(int[] waveForm,int delay);
    }

    private Reactions myReactions;
    public void setReactions(Reactions reac){
        this.myReactions=reac;
    }



    // levels of the alarms
    public static final int wakeUpLevel=97884;
    public static final int normalLevel=7884;

    // types of alarms
    public static final int overTimeAlarm=78423; // --a target amount of time elapsed with out identifying an breath cycle--
    public static final int smallFluxAlarm=32433;// --breath flux is seen to be smaller than the target level--


    // sync on alarmManager instance, to make sure time stamping is correct.
    public synchronized void checkSmallFlux(int[] breathFlux,int[] waveForm){
        long current_TimeStamp=System.nanoTime();
        if ((current_TimeStamp-previous_TimeStamp)>7000000000L){   // minimum alarm separation: 7 seconds
            if (breathFlux[0]< BreathConstants.minFlux) {
//                Log.i(BreathConstants.ALARMS_BREATH,"small flux!"+Thread.currentThread().getName());
                int[] tempFluxes={breathFlux[0],BreathConstants.minFlux};
                previous_TimeStamp=current_TimeStamp;
                if (null!=myReactions) myReactions.onSmallFlux(waveForm,tempFluxes);
            }
        }
    }


    public synchronized void checkOverTime(int timer,int[] waveForm){
        long current_TimeStamp=System.nanoTime();
        if ((current_TimeStamp-previous_TimeStamp)>5000000000L){   // minimum alarm separation: 5 seconds
            if (timer>BreathConstants.minBreathInterval) {
//                Log.i(BreathConstants.ALARMS_BREATH,"overtime!"+Thread.currentThread().getName());
                previous_TimeStamp=current_TimeStamp;
                if (null!=myReactions) myReactions.onOverTime(waveForm,timer);
            }
        }
    }


    long previous_TimeStamp=System.nanoTime();


}
