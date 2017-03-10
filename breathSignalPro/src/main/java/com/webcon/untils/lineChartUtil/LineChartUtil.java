package com.webcon.untils.lineChartUtil;

import android.graphics.Color;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.webcon.untils.BreathConstants;

import java.util.ArrayList;

/**
 * Created by Administrator on 16-1-14.
 */
public class LineChartUtil {



    private static final String TAG= BreathConstants.LINE_LOG;



    // constructors
    public LineChartUtil(){}
    public LineChartUtil(float OUT_OF_SIGHT_VALUE,float maxV,float minV){
        this.OUT_OF_SIGHT_VALUE=OUT_OF_SIGHT_VALUE;
        this.SPECTRO_MAX=maxV;
        this.SPECTRO_MIN=minV;
    }
    public void setBounds(float OUT_OF_SIGHT_VALUE,float maxV,float minV){
        this.OUT_OF_SIGHT_VALUE=OUT_OF_SIGHT_VALUE;
        this.SPECTRO_MAX=maxV;
        this.SPECTRO_MIN=minV;
    }
    public void resizeChart(LineChart mChart){
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMaxValue(SPECTRO_MAX);
        leftAxis.setAxisMinValue(SPECTRO_MIN);
    }


    // OUT_OF_SIGHT_VALUE should be larger than SPECTRO_MAX,
    // this is for debugging purpose to show position indicators
    private float OUT_OF_SIGHT_VALUE=777;
    private float SPECTRO_MAX=120;
    private float SPECTRO_MIN=-120;


    public void initChart(LineChart mChart){
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
        leftAxis.setAxisMaxValue(SPECTRO_MAX);
        leftAxis.setAxisMinValue(SPECTRO_MIN);
        leftAxis.setDrawGridLines(false);// grid disappear
        Log.i(TAG,"initChart in "+ Thread.currentThread().getName());
    }


    /**
     * create data set form input data
     *
     * @param ints  data to draw line
     * @param localMinPosi   data of position indicators
     * @return
     */
    public LineData initDataFromArray(int[] ints,int[] localMinPosi){

        int total_Length=ints.length;
        int local_min_numbers=localMinPosi.length;

        LineData mLdata;

        // arraylist of y-x entry
        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();

        // arraylist of y-x entry--> to show Local minimum indicators--> for debugging
        ArrayList<Entry> valsComp2 = new ArrayList<Entry>();

        // arraylist of x-label
        ArrayList<String> xVals = new ArrayList<String>();

        for(int i=0;i<total_Length;i++) {
            Entry c1e1 = new Entry((float) ints[i], i); //  y(float)-x(int) pairs
            valsComp1.add(c1e1);//  add entry ( y(float)-x(int) pair )
            xVals.add(i / 5f + "");  //string as labels
        }


        // set 2nd line to show Local Minimum indicators
        if (local_min_numbers>0) {
            int local_min_index=0;
            for (int i = 0; i < total_Length; i++) {
                Entry c1e2;
                if (i==localMinPosi[local_min_index]) {
                    c1e2 = new Entry((float) ints[i], i);
                    local_min_index=(local_min_index+1)>(local_min_numbers-1)?local_min_numbers-1:local_min_index+1;
                    // 以免遍历时  超过int[] localMinPosi的 array index
                } else {
                    c1e2 = new Entry(OUT_OF_SIGHT_VALUE, i);
                }
                valsComp2.add(c1e2);
            }
        } else{
            for (int i = 0; i < total_Length; i++) {
                Entry c1e2= new Entry(OUT_OF_SIGHT_VALUE, i);
                valsComp2.add(c1e2);
            }
        }


        // create line-data-set
        LineDataSet setComp1 = new LineDataSet(valsComp1, "signal 1");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
//        setComp1.setDrawCubic(true);//smooth lineChart
//        setComp1.setDrawCircles(false);// no circles
        setComp1.setDrawCircles(true);// draw circles
        setComp1.setCircleSize(3);
//        setComp1.setLineWidth(0);

        LineDataSet setComp2 = new LineDataSet(valsComp2, "indicators");
        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp2.setDrawCircles(true);// circles
        setComp2.setCircleColor(Color.rgb(200,0,0));
        setComp2.setLineWidth(0);
        setComp2.setColor(Color.rgb(240,240,240));  // set the line color to match the background so that only the indicators are shown

        // a plot can have multiple lines, by using line-data-set array
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(setComp1);
        dataSets.add(setComp2);

        // creat line-data with x-labels + line-data-set array
        LineData data = new LineData(xVals, dataSets);
        mLdata=data;
        Log.i(TAG,"initDataFromArray in "+ Thread.currentThread().getName());
        return mLdata;
    }

    public void plotLine1(LineChart mChart,LineData mLdata){
        mChart.setData(mLdata);

        Legend l = mChart.getLegend();  //set legend position
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);

        mChart.invalidate(); // refresh
        Log.i(TAG,"plotLine in "+ Thread.currentThread().getName());
    }





}
