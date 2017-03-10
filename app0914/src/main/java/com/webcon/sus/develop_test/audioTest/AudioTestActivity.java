package com.webcon.sus.develop_test.audioTest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.webcon.sus.demo.R;
import com.webcon.sus.utils.AudioUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class AudioTestActivity extends AppCompatActivity  {

    
    private Button bn_start;
    private Button bn_stop;
    private Button bn_play;
    private AudioUtils mAudioUtils;


    private Button bn_start2;
    private Button bn_stop2;
    private Button bn_play2;
    private AudioUtils2 mAudioUtils2;


    public void onEventMainThread(AudioGraphEvent e){
        plotLine1(mChart,mLdata);
    }

    private static class MyHandler extends Handler{
        private final WeakReference<AudioTestActivity> activity;
        public MyHandler(AudioTestActivity a){
            activity=new WeakReference<AudioTestActivity>(a);
        }

        @Override
        public void handleMessage(Message msg) {
            AudioTestActivity instance=activity.get();
            instance.plotLine1(mChart,mLdata);
        }

    }

    private final MyHandler handler=new MyHandler(this){
    };


    public static final int plotLength=1200;
    public static DataArray audioWave=new DataArray(plotLength);


    public static void updateGraph(short[] shortArray){
        audioWave.pushData(shortArray);
        mLdata=initData2(audioWave.getWaveArray());
    }


    static LineChart mChart;  // show the graph ...
    static LineData mLdata;


    private  void initChart(LineChart mChart){
        mChart.setDescription("");
        //   mChart.setDrawGridBackground(false);  // 之后设置backgroundcolor才有效
        mChart.setDrawBorders(true);//?? drawing the chart borders (lines surrounding the chart).
        // set up X-axis
        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawGridLines(false);// grid disappear
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceBetweenLabels(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x-axis at bottom

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawTopYLabelEntry(false);
        rightAxis.setEnabled(false);// right axis disappear
        rightAxis.setDrawGridLines(false);// grid disappear
        rightAxis.setDrawAxisLine(false); //  ????
        rightAxis.setStartAtZero(false);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setStartAtZero(false);
        leftAxis.setAxisMaxValue(1000f);
        leftAxis.setAxisMinValue(-500f);
        leftAxis.setDrawGridLines(false);// grid disappear
    }


    private static LineData initData2(short[] shortArray) {
        LineData mLdata;

        // arraylist of y-x entry
        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();

        // arraylist of x-label
        ArrayList<String> xVals = new ArrayList<String>();


        for (int i = 0; i < plotLength; i++) {
            Entry c1e1 = new Entry((float) shortArray[i], plotLength-1-i); //  y(float)-x(int) pairs
            valsComp1.add(c1e1);//  add entry ( y(float)-x(int) pair )
            xVals.add(i / 10f + "");  //string as labels
        }

        // create line-data-set
        LineDataSet setComp1 = new LineDataSet(valsComp1, "signal 1");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
//        setComp1.setDrawCubic(true);//smooth lineChart
        setComp1.setDrawCircles(false);// no circles

        // a plot can have multiple lines, by using line-data-set array
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(setComp1);

        // creat line-data with x-labels + line-data-set array
        LineData data = new LineData(xVals, dataSets);
        mLdata = data;
        return mLdata;
    }

    private void plotLine1(LineChart mChart, LineData mLdata) {
        mChart.setData(mLdata);

        Legend l = mChart.getLegend();  //set legend position
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);

        mChart.invalidate(); // refresh
    }


    @Override
    public void onPause(){
        mAudioUtils.release();
        mAudioUtils2.release();
        super.onPause();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_test);


        EventBus.getDefault().register(this);
//        int nret = init();
//        Log.i("native","============== init > nret " + nret );


        bn_start=(Button)findViewById(R.id.button_start);
        bn_stop=(Button)findViewById(R.id.button2_stop);
        bn_play=(Button)findViewById(R.id.button_play);

        bn_start2=(Button)findViewById(R.id.button);
        bn_stop2=(Button)findViewById(R.id.button2);
        bn_play2=(Button)findViewById(R.id.button3);


        mChart = (LineChart) findViewById(R.id.chart1);
        initChart(mChart);
        mLdata=initData2(audioWave.getWaveArray());
        plotLine1(mChart,mLdata);


        //录音工具
        mAudioUtils = new AudioUtils(null);  // IRecordData=null 表示本地文件

        //对比编码解码前后的 录音工具
        mAudioUtils2 = new AudioUtils2(null);  // IRecordData=null 表示本地文件


        bn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mAudioUtils.startRecord();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("audio", "单双声道问题？？");
                }
            }
        });

        bn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioUtils.stopRecord();
            }
        });

        bn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioUtils.startPlay();
            }
        });


        bn_start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioUtils2.startRecord();
                // coolpad dazen 不支持，单声道AudioTrack
//                int trackBufferSize = AudioTrack.getMinBufferSize(
//                        AudioUtils.SAMEPLE_RATE_HZ, AudioUtils.CHANNEL_CONFIG, AudioUtils.AUDIO_FORMAT);
//                AudioTrack mTrack = new AudioTrack(AudioUtils.STREAM_TYPE, AudioUtils.SAMEPLE_RATE_HZ,
//                        AudioUtils.CHANNEL_CONFIG, AudioUtils.AUDIO_FORMAT, trackBufferSize, AudioUtils.STREAM_MODE);
//                TransferDecoding mEncoder = new TransferDecoding();
//                try {
//                    TransferDecoding mEncoder = new TransferDecoding();
//                } catch (Error e) {
//                    e.printStackTrace();
//                    Log.e("G722", Log.getStackTraceString(e));
//                    Toast.makeText(AudioTestActivity.this, "tttttt", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        bn_stop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioUtils2.stopRecord();
            }
        });

        bn_play2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioUtils2.startPlay();
            }
        });






    }

}
