package com.webcon.sus.develop_test.audioTest;

import android.media.AudioRecord;
import android.util.Log;

import com.webcon.g722.TransferDecoding;
import com.webcon.wp.utils.WPApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import de.greenrobot.event.EventBus;
import vieboo.test.record.utils.IRecordData;

/**
 * Created by Administrator on 15-9-23.
 */
public class AudioRecorder2 {

    private static final String TAG = "AudioRecorder2";
    // 文件输出流
    private FileOutputStream fos;  //音频文件 in byte
    private Writer out;   //字符流文件


    // 录音工具
    private AudioRecord mAudioRecord;

    // 录音数组的长度
    private final int bufferSize = AudioUtils2.RECORD_BUFFER_SIZE;
    // 原始录音保存的short数组
    private byte[] buffer;
    // 正在录音标识
    private boolean isRecording = false;
    // 应用类型，本地/网络
    private int applyType;
    // 编码工具
    private TransferDecoding mEncoder;
    // 回调接口，用于网络
    private IRecordData callback;

    public AudioRecorder2(IRecordData callback) {
        if(callback == null){
            //本地
            applyType = AudioUtils2.APPLY_TYPE_LOCAL;
            createFile();
        }else{
            //网络
            this.callback = callback;
            applyType = AudioUtils2.APPLY_TYPE_ONLINE;
        }
        init();
    }

    private void init(){
        buffer = new byte[bufferSize];
        initRecorder();
    }

    private void createFile(){
        // file to write sound in bytes
        File file = new File(AudioUtils2.WAV_FILE);
        if(file.exists()){
            file.delete();
        }
        try {
            if(file.createNewFile()){
                fos = new FileOutputStream(file);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        // file to write sound in strings
        File file_s = new File(AudioUtils2.WAV_FILE1);
        if(file_s.exists()){
            file_s.delete();
        }
        try {
            if(file_s.createNewFile()){
                out = new FileWriter(file_s);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 初始化
     */
    private void initRecorder(){
        //初始化编码库
        mEncoder = new TransferDecoding();
        mEncoder.g722_encoder_init(AudioUtils2.BITRATE, AudioUtils2.SAMEPLE_RATE_HZ);
        //获得录音缓冲区
        int recordBufferSize = AudioRecord.getMinBufferSize(
                AudioUtils2.SAMEPLE_RATE_HZ, AudioUtils2.CHANNEL_CONFIG, AudioUtils2.AUDIO_FORMAT);


        //获得录音对象
        mAudioRecord = new AudioRecord(
                AudioUtils2.AUDIO_SOURCE, AudioUtils2.SAMEPLE_RATE_HZ,
                AudioUtils2.CHANNEL_CONFIG, AudioUtils2.AUDIO_FORMAT, recordBufferSize * 10);  //以前recordBufferSize * 10 -->??
    }


    //--------------------------------------
    public void start(){
        if(isRecording){
            return;
        }
        isRecording = true;
        new RecordThread().start();
    }

    public void stop(){
        isRecording = false;
    }

    private void release(){
        if(WPApplication.DEBUG){
            Log.i(TAG, "---release---");
        }
        mAudioRecord.stop();
        mAudioRecord.release();
        mAudioRecord = null;
        mEncoder.g722_encoder_release();
        try {
            if(fos != null){
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void execute(byte[] buffer, int length){
        //音量显示： 722编码前
        short[] newData_volumne = new short[length/2];
        long ave=0;
        for(int vi=0;vi<length;vi=vi+2){
            short a1=buffer[vi];
            short a2=buffer[vi+1];
//            Log.i("CCC","volume low8="+a1+"   volume high8="+a2);
//            newData_volumne[vi/2]=(short)(a1+a2<<8);
            newData_volumne[vi/2]=(short) (((a2 & 0xff) << 8) | (a1 & 0xff));
            Log.i("CCC","CONVERTED="+newData_volumne[vi/2]);
            ave=ave+newData_volumne[vi/2]*newData_volumne[vi/2];
        }

        AudioTestActivity.updateGraph(newData_volumne);  // push data to short[] audioWave
        EventBus.getDefault().post(new AudioGraphEvent());

        ave=ave/(newData_volumne.length);
        double volume = 10 * Math.log10((double)ave);
        Log.i("BBB","volume="+volume);



        //查看编码前数据
//        dumpDataBBB_short(newData_volumne);
//        dumpDataBBB(buffer);


//        //数据处理
//        byte[] newData = new byte[length];
//        int enLength = mEncoder.g722_encoder(newData, buffer, length);
//        Log.i("BBB", "enLength"+enLength+"  ---length"+length);
//        byte[] enData = new byte[enLength];
//        System.arraycopy(newData, 0, enData, 0, enLength);


        //音量显示： 722编码后
//        short[] newData_volumne = new short[enData.length/2];
//        long ave=0;
//        for(int vi=0;vi<enData.length;vi=vi+2){
//            short a1=enData[vi];
//            short a2=enData[vi+1];
//            Log.i("BBB","volume low8="+a1+"   volume high8="+a2);
//            newData_volumne[vi/2]=(short)(a1+a2<<8);
//            ave=ave+newData_volumne[vi/2]*newData_volumne[vi/2];
//        }
//        ave=ave/(newData_volumne.length);
//        double volume = 10 * Math.log10((double)ave);


        //保存到本地，或者发送到网络
        if(applyType == AudioUtils2.APPLY_TYPE_LOCAL && fos != null){
            try {
//                fos.write(enData, 0, enLength); //写入编码数据
                fos.write(buffer, 0, length); //直接写入原始PCM数据
                fos.flush();

//                String res=new String(buffer,"UTF-8");
//                out.write(res);
//                out.flush();
                out.write(getChars(buffer));
                out.flush();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (applyType == AudioUtils2.APPLY_TYPE_ONLINE && callback != null){
//            callback.onRecordData(enData, enLength);
        }
    }

    //--------------------------
    /**
     * 录音线程
     *
     */
    class RecordThread extends Thread{
        @Override
        public void run(){
            if(mAudioRecord == null){
                release();
            }
            //开始录音
            mAudioRecord.startRecording();
            int length = 0;
            try{
                while(isRecording){
                    length = mAudioRecord.read(buffer, 0, bufferSize);
                    Log.i("CCC","RecordThread--length:"+length);   //-----92
                    //--如何搞出音量？？
                    execute(buffer, length);    // TODO-----recording length???
                }
            }finally {
                release();
            }
        }
    }


    /**
     * 测试函数-------
     * @param data
     */
    private void dumpDataBBB(byte[] data) {
        Log.i("BBB", "[----------------data  start----------------]");
        if (data.length > 300) {
            for (int i = 0; i < 300; i++) {
                Log.i("BBB", "index"+i+"byte data:" + data[i]);
            }
        } else {
            for (int i = 0; i < data.length; i++) {
                Log.i("BBB", "index"+i+"  byte data:" + data[i]);
            }
        }
        Log.i("BBB", "[----------------data  end-----------------]");
    }

    private void dumpDataBBB_short(short[] data) {
        Log.i("BBB", "[----------------data  start----------------]");
        if (data.length > 300) {
            for (int i = 0; i < 300; i++) {
                Log.i("BBB", "index"+i+"byte data:" + data[i]);
            }
        } else {
            for (int i = 0; i < data.length; i++) {
                Log.i("BBB", "index"+i+"  byte data:" + data[i]);
            }
        }
        Log.i("BBB", "[----------------data  end-----------------]");
    }


    private String getChars (byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(int ii=0;ii<bytes.length;ii=ii+1){
            sb.append(bytes[ii]+"\n");
        }
        return sb.toString();
    }


}
