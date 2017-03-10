package test1.nh.com.demos1.audioTest;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

import test1.nh.com.demos1.R;

public class AudioActivity extends AppCompatActivity {

    private Button bn_start2;
    private Button bn_stop2;
    private Button bn_play2;
    private AudioUtils2 mAudioUtils2;


    private Button bn_start3;
    private AudioUtils3 mAudioUtils3;
    public static final int plotLength=2400;
    DataArray audioWave=new DataArray(plotLength);
    LineChart mChart;  // show the graph ...
    LineData mLdata;


    Handler graphHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            plotLine1(mChart,mLdata);
        }
    };


    @Override
    public void onPause(){
        mAudioUtils2.release();
        mAudioUtils3.release();
        super.onPause();
    }



    public static void start(Context context){
        final Intent intent=new Intent(context,AudioActivity.class);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        bn_start2=(Button)findViewById(R.id.button);
        bn_stop2=(Button)findViewById(R.id.button2);
        bn_play2=(Button)findViewById(R.id.button3);

        bn_start3=(Button)findViewById(R.id.button4);

        //对比编码解码前后的 录音工具
        mAudioUtils2 = new AudioUtils2(null);  // IRecordData=null 表示本地文件


        //  录音工具---> 传入录音数据处理类
        mAudioUtils3 = new AudioUtils3(new IRecordData() {
            @Override
            public void onRecordData(byte[] data, int dataLength) {
                Log.i("AAA","data length"+dataLength+"   --"+data.length+"  Thead:"+Thread.currentThread().getName());

                short[] newData_volumne = new short[dataLength/2];  //转化为short
                long ave=0;
                for(int vi=0;vi<dataLength;vi=vi+2){
                    short a1=data[vi];
                    short a2=data[vi+1];
                    newData_volumne[dataLength/2-vi/2-1]=(short) (((a2 & 0xff) << 8) | (a1 & 0xff));  // dataLength/2-vi/2-1  this is correct!!!  or  vi/2
//                    Log.i("AAA","CONVERTED="+newData_volumne[vi/2]);
                    ave=ave+newData_volumne[vi/2]*newData_volumne[vi/2];
                }
                ave=ave/(newData_volumne.length);   // volume
                double volume = 10 * Math.log10((double)ave);
                Log.i("AAA","volume="+volume);

                audioWave.pushData(newData_volumne);
                mLdata=initData2(audioWave.getWaveArray());
                graphHandler.sendEmptyMessage(1);
            }
        });




        bn_start2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioUtils2.startRecord();
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


        bn_start3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioUtils3.startRecord();
            }
        });


        mChart=(LineChart)findViewById(R.id.chart1);
        initChart(mChart);
        mLdata=initData2(audioWave.getWaveArray());
        plotLine1(mChart,mLdata);

    }






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
        leftAxis.setAxisMaxValue(2000f);  // 1000f
        leftAxis.setAxisMinValue(-1000f);  // -500f
        leftAxis.setDrawGridLines(false);// grid disappear
    }


    private LineData initData2(short[] shortArray) {
        LineData mLdata;

        // arraylist of y-x entry
        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();

        // arraylist of x-label
        ArrayList<String> xVals = new ArrayList<String>();


        for (int i = 0; i < plotLength; i++) {
            Entry c1e1 = new Entry((float) shortArray[i], i); //  y(float)-x(int) pairs   ??plotLength-1-i
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

}
