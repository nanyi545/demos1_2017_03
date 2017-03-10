package test1.nh.com.demos1.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.DrawerActivity;
import test1.nh.com.demos1.utils.seekBarHelper.SeekBarNumericTransformerHelper;

/**
 * Created by Administrator on 15-9-30.
 */
public class MPchartDemoFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section4";


    private Context mContext;

    private LineChart mChart;
    private LineData mLdata;

    private LineChart mChart2;
    private LineData mLdata2;

    private BarChart mBarChart;
    private BarData mBardata;

    private PieChart mPieChart;


    private LineChart mChart_playback;
    private LineData pb_Ldata;
    private float[] total_array=new float[750];

    DiscreteSeekBar seekBar;


    //empty constructor
    public MPchartDemoFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MPchartDemoFragment newInstance(int sectionNumber) {
        MPchartDemoFragment fragment = new MPchartDemoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    Handler updateUI = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            seekBar.setProgress(msg.what);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_mpchart_drawer, container, false);

        // plot 1
        mChart = (LineChart) rootView.findViewById(R.id.chart1);
        initChart(mChart);
        mLdata = initData1();
        plotLine1(mChart, mLdata);

        // plot 2
        mChart2 = (LineChart) rootView.findViewById(R.id.chart2);
        initChart(mChart2);
        mLdata2 = initData2();
        plotLine1(mChart2, mLdata2);

        // plot 3-----barChart----
        mBarChart = (BarChart) rootView.findViewById(R.id.barChart1);
        initBarChart(mBarChart);
        int[] counts = new int[]{1, 3, 4, 5, 7, 14, 18, 19, 13, 6, 3, 1};
        mBardata = generateBarData(1, counts, 12);
        mBarChart.setData(mBardata);


        // plot 4-----pie chart----
        mPieChart = (PieChart) rootView.findViewById(R.id.sta_pchart);
        initPieChart(mPieChart);
        setData(mPieChart, 12);  // show anomaly percentage


        // plot 5 ---playback chart
        mChart_playback = (LineChart) rootView.findViewById(R.id.chart3_pb);
        initPBChart(mChart_playback);
        prepareTotalData();
        pb_Ldata = preparePlaybackData(1);
        plotLine1(mChart_playback, pb_Ldata);

        seekBar = (DiscreteSeekBar) rootView.findViewById(R.id.chart3_seekBar);
        //  setIndicatorFormatter  /setMin/setMax  /setNumericTransformer

//        seekBar.setMin(50);
//        seekBar.setMax(250);
        seekBar.setNumericTransformer(new SeekBarNumericTransformerHelper(new GregorianCalendar()));
//        seekBar.setIndicatorPopupEnabled(false);

        seekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                // fromUser--> drag by user:true    seekBar.setProgress(int) by code:false
//                Log.i("AAA", "dragging..." + value + "  fromUser?" + fromUser+"   in thread:"+Thread.currentThread().getName());// if not set. in main thread!!
                pb_Ldata = preparePlaybackData(value);    // data preparation needs to be put in worker thread!!!
                plotLine1(mChart_playback, pb_Ldata);

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {}
        });


        // ---test of waveform "play back"
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int ii = 1; ii < 50; ii++) {
                    updateUI.sendEmptyMessage(ii);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //设置activity label
        mContext = activity;

        try {    //  called by DrawerActivity
            ((DrawerActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        } catch (Exception e) {
            e.printStackTrace();
//            Log.i("AAA", "MPCHARTfragment called not by DrawerActivity");
        }

    }


    private void initBarChart(BarChart barChart) {

        XAxis xAxis = barChart.getXAxis();
        xAxis.setDrawGridLines(false);// grid disappear
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x-axis at bottom

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);// grid disappear
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);// grid disappear

        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);

    }

    private void initChart(LineChart mChart) {

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

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);// grid disappear
    }

    private LineData initData1() {
        LineData mLdata;
        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();

        Entry c1e1 = new Entry(100.000f, 0); // 0 == quarter 1
        valsComp1.add(c1e1);
        Entry c1e2 = new Entry(50.000f, 1); // 1 == quarter 2 ...
        valsComp1.add(c1e2);
        Entry c1e3 = new Entry(120.000f, 2); // 2 == quarter 3
        valsComp1.add(c1e3);
        Entry c1e4 = new Entry(150.000f, 3); // 3 == quarter 4 ...
        valsComp1.add(c1e4);

        LineDataSet setComp1 = new LineDataSet(valsComp1, "Company 1");
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);

        setComp1.setDrawCubic(true);//smooth lineChart

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(setComp1);

        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("1.Q");
        xVals.add("");
        xVals.add("3.Q");
        xVals.add("4.Q");

        LineData data = new LineData(xVals, dataSets);
        mLdata = data;
        return mLdata;
    }

    private LineData initData2() {
        LineData mLdata;

        // arraylist of y-x entry
        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();

        // arraylist of x-label
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < 750; i++) {
            Entry c1e1 = new Entry((float) Math.sin(i / 10f), i); //  y(float)-x(int) pairs
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


    // prepared the total data pool from which the display data will be extracted and displayed
    private void prepareTotalData(){
        for (int i = 0; i < 750; i++) {
            total_array[i] = ((float) Math.sin(i / 10f)); //
        }
    }


    private LineData preparePlaybackData(int startIndex) {
        LineData mLdata;

        // arraylist of y-x entry
        ArrayList<Entry> valsComp1 = new ArrayList<Entry>();

        // arraylist of x-label
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < 300; i++) {
            Entry c1e1 = new Entry(total_array[i+startIndex], i); //  y(float)-x(int) pairs
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


    /**
     * generate barChart data
     */
    protected BarData generateBarData(int dataSets, int[] counts, int count) {

        ArrayList<BarDataSet> sets = new ArrayList<BarDataSet>();

        for (int i = 0; i < dataSets; i++) {  // sets of data, usually=1

            ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

//            entries = FileUtils.loadEntriesFromAssets(getActivity().getAssets(), "stacked_bars.txt");

            for (int j = 0; j < count; j++) {    // entries for one particular set
                entries.add(new BarEntry((float) counts[j], j));
            }

            BarDataSet ds = new BarDataSet(entries, getLabel(i));
            int[] colors = new int[]{
                    Color.rgb(192, 255, 140)
            };

            ds.setColors(colors);  //ColorTemplate.VORDIPLOM_COLORS
            sets.add(ds);
        }

        BarData d = new BarData(ChartData.generateXVals(0, count), sets);
        return d;
    }


    /**
     * set piechart data , anaValue%
     *
     * @param mPieChart
     * @param anaValue
     */
    private void setData(PieChart mPieChart, int anaValue) {
        mPieChart.setCenterText(generateCenterSpannableText("text at center\npercent\n" + anaValue + "%"));

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        yVals1.add(new Entry((float) anaValue, 0));
        yVals1.add(new Entry((float) (100 - anaValue), 0));


        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("1");
        xVals.add("2");

        PieDataSet dataSet = new PieDataSet(yVals1, null);
        dataSet.setSliceSpace(2f);
        dataSet.setSelectionShift(5f);
        dataSet.setDrawValues(false);  // does not show values

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(getResources().getColor(R.color.Red500));  // slice 1 color
        colors.add(getResources().getColor(R.color.Teal200));  // slice 2 color

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);

        data.setValueTextColor(Color.WHITE);
        mPieChart.setData(data);

        // undo all highlights
        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }

    private void initPieChart(PieChart mPieChart) {
        mPieChart.setUsePercentValues(true);
        mPieChart.setDrawSliceText(false); // hide labels

        mPieChart.setRotationAngle(270f);
        // enable rotation of the chart by touch
        mPieChart.setRotationEnabled(false);

        Legend l1 = mPieChart.getLegend();//remove the legend
        l1.setEnabled(false);  //remove the legend

        mPieChart.setDescription("");
        mPieChart.setExtraOffsets(5, 10, 5, 5);

        mPieChart.setDragDecelerationFrictionCoef(0.95f);

        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColorTransparent(true);

        mPieChart.setTransparentCircleColor(Color.WHITE);

        mPieChart.setTransparentCircleAlpha(0);
        mPieChart.setTransparentCircleRadius(71f);

        mPieChart.setHoleRadius(78f);

        mPieChart.setDrawCenterText(true);

    }


    private SpannableString generateCenterSpannableText(String str) {
        SpannableString s = new SpannableString(str);
//        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
//        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
//        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
//        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
//        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
//        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }


    private String[] mLabels = new String[]{"Company A", "Company B", "Company C", "Company D", "Company E", "Company F"};
//    private String[] mXVals = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec" };

    private String getLabel(int i) {
        return mLabels[i];
    }


    private void initPBChart(LineChart mChart) {

        //   mChart.setDrawGridBackground(false);  // 之后设置backgroundcolor才有效
        mChart.setDrawBorders(true);//?? drawing the chart borders (lines surrounding the chart).

        // set up X-axis
        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.RED);
        xAxis.setDrawGridLines(false);// grid disappear
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceBetweenLabels(1);
        xAxis.setPosition(XAxis.XAxisPosition.TOP); //x-axis at bottom

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawTopYLabelEntry(false);
        rightAxis.setEnabled(false);// right axis disappear
        rightAxis.setDrawGridLines(false);// grid disappear
        rightAxis.setDrawAxisLine(false); //  ????

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);// grid disappear
    }


}
