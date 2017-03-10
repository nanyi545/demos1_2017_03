package test1.nh.com.demos1.audioTest;

import android.media.AudioRecord;
import android.util.Log;


/**
 * Created by Administrator on 15-9-23.
 */
public class AudioRecorder3 {

    private static final String TAG = "AudioRecorder3";

    // 录音工具
    private AudioRecord mAudioRecord;

    // 录音数组的长度
    private final int bufferSize = AudioUtils3.RECORD_BUFFER_SIZE;
    // 原始录音保存的short数组
    private byte[] buffer;
    // 正在录音标识
    private boolean isRecording = false;

    // 回调接口----
    private IRecordData callback;

    public AudioRecorder3(IRecordData callback) {
        this.callback = callback;
        init();
    }

    private void init(){
        buffer = new byte[bufferSize];
        initRecorder();
    }


    /**
     * 初始化
     */
    private void initRecorder(){
        //初始化编码库
        //获得录音缓冲区
        int recordBufferSize = AudioRecord.getMinBufferSize(
                AudioUtils3.SAMEPLE_RATE_HZ, AudioUtils3.CHANNEL_CONFIG, AudioUtils3.AUDIO_FORMAT);
        Log.i("CCC","recordBufferSize:"+recordBufferSize);


        //获得录音对象
        mAudioRecord = new AudioRecord(
                AudioUtils3.AUDIO_SOURCE, AudioUtils3.SAMEPLE_RATE_HZ,
                AudioUtils3.CHANNEL_CONFIG, AudioUtils3.AUDIO_FORMAT, recordBufferSize );  //以前recordBufferSize * 10 -->??
    }


    //--------------------------------------
    public synchronized void start(){
        if(isRecording){
            return;
        }
        isRecording = true;
        new RecordThread().start();
    }

    public synchronized void stop(){
        isRecording = false;
    }

    private void release(){
        mAudioRecord.stop();
        mAudioRecord.release();
        mAudioRecord = null;
    }

    private void execute(byte[] buffer, int length){
        if (callback!=null)
        callback.onRecordData(buffer,length);
        //音量显示： 722编码前
//        short[] newData_volumne = new short[length/2];
//        long ave=0;
//        for(int vi=0;vi<length;vi=vi+2){
//            short a1=buffer[vi];
//            short a2=buffer[vi+1];
////            Log.i("BBB","volume low8="+a1+"   volume high8="+a2);
////            Log.i("BBB","byte   low8="+buffer[vi]+"   byte   low8="+buffer[vi+1]);
////            newData_volumne[vi/2]=(short)(a1+a2<<8);
//            newData_volumne[vi/2]=(short) (((buffer[vi+1] & 0xff) << 8) | (buffer[vi] & 0xff));
//            ave=ave+newData_volumne[vi/2]*newData_volumne[vi/2];
//        }
//        ave=ave/(newData_volumne.length);
//        double volume = 10 * Math.log10((double)ave);
//        Log.i("BBB","volume="+volume);

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
                    Log.i("CCC","RecordThread--length:"+length+" COMPARED with bufferSize:"+bufferSize);   //-----92
                    //--如何搞出音量？？
                    execute(buffer, length);
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
