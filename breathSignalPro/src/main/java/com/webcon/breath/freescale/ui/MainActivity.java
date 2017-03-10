package com.webcon.breath.freescale.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.webcon.dataProcess.DataHandler;
import com.webcon.dataProcess.DataTransmitter;
import com.webcon.dataProcess.dataUtils.utilEntities.BreathCycleCondition;
import com.webcon.untils.BreathConstants;
import com.webcon.untils.alarmManagement.AlarmManager;
import com.webcon.untils.alarmManagement.alarmEntities.BreathAlarm;
import com.webcon.untils.alarmManagement.alarmEntities.OverTimeAlarm;
import com.webcon.untils.alarmManagement.alarmEntities.SmallFluxAlarm;
import com.webcon.untils.fileOperation.recordsManagement.IOmanager;
import com.webcon.untils.fileOperation.recordsManagement.IntArrayIO;
import com.webcon.untils.lineChartUtil.LineChartUtil;
import com.webcon.wp.utils.CCallbackMethod;
import com.webcon.wp.utils.NativeInterface;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.Callable;

import rm.module_net.R;
import rx.Observer;
import serial_port.demo.SerialTestActivity;


public class MainActivity extends Activity {




    int timeElapsed;
    int flux;
    int signalState;
    TextView tv_timer;

    LineChart mChart;  // show the graph ...
    LineData mLdata;
    LineChartUtil lineChartUtil=new LineChartUtil();

    int[] int_min_index={50,100,150,200,250};
    int[] int_min_index2={};
    BreathCycleCondition recordCondition_1=new BreathCycleCondition(3300,12,10);  // used in matlab

    private IviewSpectro graphSpectr=new IviewSpectro(){
        @Override
        public void prepareGraphData(int[] ints, int[] indicators, int timer, int flux_, int signalState_) {
            //---------------------
            //---prepare data
            //---------------------
//            mLdata=lineChartUtil.initDataFromArray(ints,int_min_index2); // input static(fixed) position indicators
//            mLdata=lineChartUtil.initDataFromArray(ints, ArrayAnalyser.findLocalMins(ints)); // input dynamic position indicators--by using simple local minimum

//            mLdata=lineChartUtil.initDataFromArray(ints, ArrayAnalyser.findLocalMins_refined(ints,sinWaveTestCondition_1));
            mLdata=lineChartUtil.initDataFromArray(ints,indicators);

            timeElapsed=timer;//time elapsed after the appearance of one breath cycle
            flux=flux_;  // flux of the most recent breath cycle
            signalState=signalState_; // signal state:

            uihandler.sendEmptyMessage(1);//tell the UI to draw graph
            Log.i(BreathConstants.THREAD_INDICATOR,"UpdateGraph in "+Thread.currentThread().getName());  // in RX computation thread
        }
    };


    private Handler uihandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            float timeInSeconds=timeElapsed/5.0f;
            String signal_state="";
            switch (signalState){
                case BreathConstants.LOW_SIGNAL_QUALITY:signal_state="信号接收异常，请确认传感器对着腹部";
                    break;
                case BreathConstants.NORMAL_SIGNAL_QUALITY:signal_state="信号正常";
                    break;
                case BreathConstants.SIGNAL_BUFFERING:signal_state="信号读取中";
                    break;
            }
            tv_timer.setText(""+timeInSeconds+"秒之内没有检测到呼吸"+"\n"+"最近一次呼吸通量"+flux+"\n"+signal_state);
            lineChartUtil.plotLine1(mChart, mLdata);
        }
    };


    /** Called when the activity is first created. */
    private TestThread thread;// to test communication lib
    private CCallbackMethod mCCallbackMethod;


    // handle the data
    DataHandler handler1=new DataHandler();


    int[] ints=new int[20];
    private void init_input_ints(){
        for (int ii=0;ii<20;ii++){
            ints[ii]=ii;
        }
    }
    private double sinArg=0;


    //-----read signal records------
    private Runnable recordSignalInput=new Runnable(){
        @Override
        public void run() {

            // read from asset folder
            AssetManager am =  null ;
            am = getAssets();


            lineChartUtil.setBounds(180000,65000,15000);// reset the chart size
            lineChartUtil.resizeChart(mChart);

            try {
                // --list all files in a path --
//                String[] listOfRecs=am.list("data_rec");
//                Log.i("AAA",""+listOfRecs[2]);  //  record1.data record2.data record3.data record4sto.data record5sto.data

                InputStream is = am.open("data_rec/record3.data");
                DataInputStream dataInputStream = new DataInputStream(is);
                int totalBytes = dataInputStream.available();
                int countor = 0;
                while (totalBytes > 0) {
//                    Thread.sleep(1000);
//                    int[] temp=new int[5];
//                    temp[4]=dataInputStream.readInt();
//                    temp[3]=dataInputStream.readInt();
//                    temp[2]=dataInputStream.readInt();
//                    temp[1]=dataInputStream.readInt();
//                    temp[0]=dataInputStream.readInt();
//                    handler1.dataIn(temp);

                    Log.i(BreathConstants.PROGRESS_INDICATOR,"read recording...");
                    Thread.sleep(200);
                    int[] temp=new int[1];
                    temp[0]=dataInputStream.readInt();
                    handler1.dataIn(temp);

                    totalBytes=dataInputStream.available();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    //---- sin data source------
    private Runnable sinSignalInput=new Runnable(){
        @Override
        public void run() {

            lineChartUtil.setBounds(777,120,-120);// reset the chart size
            lineChartUtil.resizeChart(mChart);


            while(true){
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int temp=(int)(Math.sin(sinArg)*100);
                int randInt=new Random().nextInt(20)-10;   // --> introduce some noise
                temp=temp+randInt;
                sinArg=sinArg+0.30;
                int[] ints_temp=new int[1];
                ints_temp[0]=temp;
                handler1.dataIn(ints_temp);
//                    handler1.dataIn(ints);
            }
        }
    };

    protected SharedPreferences wpPreferences;


    // called by UI to reset the minimum level and overwrite in the SPreference
    public void set_Min_interval(int min_interval){
        BreathConstants.minBreathInterval=min_interval;
        wpPreferences.edit().putInt(BreathConstants.MIN_BREATH_INTERVAL_KEY,min_interval).apply();
    }




    
    private void initFileReader() {
        // init dependencies for File reading
        file= new File(new File(new File(getFilesDir(), BreathConstants.RECORD_FILE_FOLDER),"2016_03_03"),"12_20.nhBreath");
        reader = new IOmanager(new IntArrayIO(file));
        reader.setReadObserver(read_observer);// set Read observer to read file -->>
    }



    //---------- for read file-------------
    Observer<Callable<int[]>> read_observer = new Observer<Callable<int[]>>() {
        @Override
        public void onCompleted() {}

        @Override
        public void onError(Throwable e) {}

        @Override
        public void onNext(Callable<int[]> func) {
            Log.i("CCC", "on next" + Thread.currentThread().getName());
            try {
                int[] aa=func.call();
                Log.i("CCC", "int array size:" + aa.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    File file;
    IOmanager reader;
    public void printFile(View view){
        reader.readArrayFromFile(file);
    }




//    // ------for data playback-------
//    private LineChart mChart_playback;
//    private LineData pb_Ldata;
//    private float[] total_array=new float[750];
//
//    DiscreteSeekBar seekBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        testDataTransmitter();

        //--init input array--
        init_input_ints();

        // access the SPreference to fetch and then set the breath Wakeup Alarm(serious alarm) alarm level
        wpPreferences = getSharedPreferences(BreathConstants.SP_NAME, Context.MODE_PRIVATE); // accessible only by this app
        int temp_interval=wpPreferences.getInt(BreathConstants.MIN_BREATH_INTERVAL_KEY,BreathConstants.minBreathIntervalDefault);
        BreathConstants.minBreathInterval=(temp_interval==BreathConstants.minBreathIntervalDefault)?BreathConstants.minBreathInterval_init:temp_interval;  // default value 10 secconds
        int temp_flux=wpPreferences.getInt(BreathConstants.MIN_FLUX_KEY,BreathConstants.minFluxDefault);
        BreathConstants.minFlux=(temp_flux==BreathConstants.minFluxDefault)?BreathConstants.minFlux_init:temp_flux;  // default value 3500 secconds


        tv_timer=(TextView) findViewById(R.id.tv_timer);
        mChart = (LineChart) findViewById(R.id.chart1);

        lineChartUtil.initChart(mChart);
        final int[] init_ints=new int[300];
        mLdata=lineChartUtil.initDataFromArray(init_ints,int_min_index2);
        lineChartUtil.plotLine1(mChart, mLdata);


//        handler1.initHandler(new ViewSpectr_Log());  // print praph by logcat....
        handler1.initHandler(graphSpectr);  // print graph to chart....

        Observer<Callable> write_observer = new Observer<Callable>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                if ("java.util.concurrent.TimeoutException".equals(e.toString())) {
                    Log.i("AAA", "write finished");
                }
            }

            @Override
            public void onNext(Callable func) {
                try {
                    func.call();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("AAA", Log.getStackTraceString(e));
                }
                Log.i("AAA", "on next" + Thread.currentThread().getName());
            }
        };


//        handler1.initFileRecording(this,write_observer);  // initialize recording to file functionality


        initFileReader();


        handler1.handleAlarms(new AlarmManager.Reactions(){
            @Override
            public void onOverTime(int[] waveForm,int delay) {
                BreathAlarm alarm=new OverTimeAlarm(new GregorianCalendar(),waveForm,delay);
                Log.i(BreathConstants.ALARMS_BREATH,alarm.toString()+"---@"+ Thread.currentThread().getName());
            }

            @Override
            public void onSmallFlux(int[] waveForm,int[] fluxes) {
                BreathAlarm alarm=new SmallFluxAlarm(new GregorianCalendar(),waveForm,fluxes);
                Log.i(BreathConstants.ALARMS_BREATH,alarm.toString()+"---@"+ Thread.currentThread().getName());
            }
        });


        // signal generation...
        // fake data source...---> sin source with noise
//        new Thread(sinSignalInput).start();

        // fake data source...---> reading from records
        new Thread(recordSignalInput).start();


////------------- to test communication lib-----------------  // currently this lib requires target SDK<14 ------ alreadt solved in station APP
        int nret = NativeInterface.getInstance().init();
        Log.i("CCC","lib init return:"+nret);

//        mCCallbackMethod = new CCallbackMethod(this);
//        thread = new TestThread();
//        thread.start();



    }





    /**
     * test of using IdataSource
     */
    private void testDataTransmitter() {
        final DataTransmitter transmitter1;
        final int[] ints = {1, 7, 9, 10, 21};

        transmitter1=new DataTransmitter();
        // test observer
        Observer<int[]> integerObserver=new Observer<int[]>() {
            @Override public void onCompleted() {}
            @Override public void onError(Throwable e) {}
            @Override public void onNext(int[] ints) {
                for (int aa=0;aa<ints.length;aa++){
                    Log.i("AAA","data handled in--"+Thread.currentThread().getName()+"---"+aa+"th integer in the array:"+ints[aa]);
                }
            }
        };

        transmitter1.setDataObserver(integerObserver);

        // test data source...
        new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    transmitter1.streamIn(ints);
                }
            }
        }).start();
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
    }



    //test of serial port
    public void jumpSerial(View v){
        SerialTestActivity.goTo(this);
    }

    // test for network.so Lib
    class TestThread extends Thread{
        @Override
        public void run()
        {
            int nret1=mCCallbackMethod.setCallBackObj();
            Log.i("CCC","++++++>set object" + nret1 );

            //  int nret = doWork("test/jnitest/CCallbackMethod","callHello","192.168.1.82",6800);
            int nret = mCCallbackMethod.doWork("com/webcon/wp/utils/CCallbackMethod","callHello","car_infoBuf");
            Log.i("CCC","++++++>doWork nret" + nret )	;

            String[] carList = new String[100] ;
            int[] carFlag = new int[100];
            int count = 0;

            while(true){
                //		count = get(carList,carFlag);
                Log.i("CCC","++++++> cout" + count );
                for(int i = 0; i < count ; i++)
                {
                    Log.i("CCC","++++++> i[" +  carList[i] + "] flag["
                            + carFlag[i] + "]");
                }
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }








}
