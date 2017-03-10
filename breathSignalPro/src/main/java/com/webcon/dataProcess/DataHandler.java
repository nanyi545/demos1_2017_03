package com.webcon.dataProcess;

import android.content.Context;
import android.util.Log;

import com.webcon.breath.freescale.ui.IviewSpectro;
import com.webcon.dataProcess.dataUtils.ArrayAnalyser;
import com.webcon.dataProcess.dataUtils.utilEntities.BreathCycleCondition;
import com.webcon.untils.BreathConstants;
import com.webcon.untils.alarmManagement.AlarmManager;
import com.webcon.untils.fileOperation.RecordsLocator;
import com.webcon.untils.fileOperation.recordsManagement.IOmanager;
import com.webcon.untils.fileOperation.recordsManagement.IntArrayIO;

import java.io.File;
import java.util.GregorianCalendar;

import rx.Observer;

/**
 * Created by Administrator on 16-1-14.
 *
 * the class to handle breath wave form data
 *
 */
public class DataHandler {



    private RecordsLocator recordLocator;
    private File writeTarget;
    private IOmanager dataWriter;


    public void initFileRecording(Context context,Observer write_observer ){
        recordLocator= new RecordsLocator(new GregorianCalendar(), context);
        writeTarget = recordLocator.getRecFile(); // create file to write records to
        dataWriter = new IOmanager(new IntArrayIO(writeTarget));
        dataWriter.setWriteObserver(write_observer);
    }





    private int timer=0;
    private int initStateCountDown=BreathConstants.SIGNAL_BUFFERING_TIME; // 初始XX sec内，认为是缓冲状态： BreathConstants.SIGNAL_BUFFERING


    //
    private AlarmManager a1=new AlarmManager();
    public void handleAlarms(AlarmManager.Reactions alarmReactions){
        a1.setReactions(alarmReactions);
    }



    //--- analyser interface----
    private interface Analyser{
        void analyse();
    }

    /**
     *  breathAlarmAnalyser is responsible to analyse the breath waveform to check if there is any type of alarm
     */
    private Analyser breathAlarmAnalyser=new Analyser(){
        @Override
        public void analyse() {
            int[] currentBreathSignal=myArray.getBreathArray(); // get breath signal

            // find the percentage of breath signal:
            double percentage=ArrayAnalyser.findBreathComponent(currentBreathSignal);
//            Log.i("AAA","breath percentage:"+percentage);
            int signalState;
            if (--initStateCountDown>0) signalState=BreathConstants.SIGNAL_BUFFERING;   // 初始XX sec内，认为是缓冲状态： BreathConstants.SIGNAL_BUFFERING
            else if (percentage>0.4) signalState=BreathConstants.NORMAL_SIGNAL_QUALITY;
            else signalState=BreathConstants.LOW_SIGNAL_QUALITY;


            // find dynamically the local minimums --- as a function of breath cycle condition
            int[] localMins_refined=ArrayAnalyser.findLocalMins_refined(currentBreathSignal,recordCondition_1);

            // ---- get breath fluxes ----
            int[] breathFlux=ArrayAnalyser.findBreathFlux(currentBreathSignal,localMins_refined);

            // ----check if there is a new breath cycle--> for timer to record the time elapsed until the next breath cycle
            previousFirst=thisFirst;
            thisFirst=localMins_refined.length<1?(previousFirst+1):(localMins_refined[0]);  // make sure localMins_refined.length is at least 1
            if ((thisFirst-previousFirst)!=1) {  // new  cycle identified!
                Log.i(BreathConstants.ALARMS_BREATH,"new cycle!"+percentage);
                timer=0;  // new cycle identified --> reset timer to 0
                if (signalState==BreathConstants.NORMAL_SIGNAL_QUALITY) a1.checkSmallFlux(breathFlux,currentBreathSignal);
            }
            timer=timer+1;  // also keep a record of time elapsed until a next breath cycle, one unit corresponds to 0.2 sec
            if (signalState==BreathConstants.NORMAL_SIGNAL_QUALITY) a1.checkOverTime(timer,currentBreathSignal);


            //----prepare data (to be used for UI)----
            viewSpectro.prepareGraphData(currentBreathSignal, localMins_refined,timer, breathFlux[0], signalState);
        }
    };


    private int previousFirst=0;
    private int thisFirst=1;

    BreathCycleCondition sinWaveTestCondition_1=new BreathCycleCondition(30,15,15);
    BreathCycleCondition recordCondition_1=new BreathCycleCondition(3300,12,10);  // used in matlab

    private DataTransmitter transmitter=new DataTransmitter();;
    public void dataIn(int[] ints){
        transmitter.streamIn(ints);
    }

    Observer<int[]> breathDataObserver =new Observer<int[]>() {
        @Override public void onCompleted() {

        }
        @Override public void onError(Throwable e) {

        }

        // all the calls here are on worker thread as specified in DataTransmitter
        @Override public void onNext(int[] ints) {
            Log.i(BreathConstants.PROGRESS_INDICATOR,"on Next...");

            myArray.pushData(ints); // update breath real time wave form

            if (null!=dataWriter){  //--- write to file---
//                Log.i("AAA","writing..");
                dataWriter.sendArray(ints);
            }


            // call analyser to check if there is any type of alarm
            breathAlarmAnalyser.analyse();




        }
    };


    private DataArray myArray=new DataArray();

    public int[] getBreathArray(){
        return myArray.getBreathArray();
    }


    public void initHandler(IviewSpectro viewSpectro){
        transmitter.setDataObserver(breathDataObserver);
        this.viewSpectro=viewSpectro;
    }


    private IviewSpectro viewSpectro;





}
