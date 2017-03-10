package test1.nh.com.demos1.audioTest;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaRecorder;

/**
 * Created by Administrator on 15-9-23.
 */
public class AudioUtils3 {
    public static final String TAG = "AudioUtils3";

    /** 音频获取源设备 */
    public static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    /** 设置音频采样率 */
    public static final int SAMEPLE_RATE_HZ = 16000;   //
    /** 设置音频录制的声道   **CHANNEL_IN_STEREO为双声道，CHANNEL_IN_MONO为单声道*/
    public static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;

    /** 音频数据格式  **PCM 16位每个样本 */
    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;   //ENCODING_PCM_16BIT ---9-21
    /** 音频流的类型 */
    public static final int STREAM_TYPE = AudioManager.STREAM_MUSIC;
    /** Track模式 */
    public static final int STREAM_MODE = AudioTrack.MODE_STREAM;
    /** 输出比特率 */
    public static final int BITRATE = 32000;           // 24k(包长60)：9-15    36800---包长92
    /** 录音缓冲区大小 */
    public static final int RECORD_BUFFER_SIZE  = 512;  // 原来640 ----9-18    both--92-->works
    /** 读取缓冲区大小 */
    public static final int TRACK_BUFFER_SIZE   = 1000;  //  原来60


    /** 音频数据回调接口 */
    private IRecordData callback;
    /** 音频录制工具 */
    private AudioRecorder3 mRecorder;


    public AudioUtils3(IRecordData callback) {
        this.callback = callback;
//        Log.i("BBB","--"+callback.toString());  // initialization success?
    }


        /* ------------ public --------------- */



    /**
     * 开始录音
     */
    public void startRecord() {
        stopRecord();   //清空语音队列
        mRecorder = new AudioRecorder3(callback);
        mRecorder.start();
    }

    /**
     * 停止录音
     */
    public void stopRecord() {
        if(mRecorder != null){
            mRecorder.stop();
            mRecorder = null;
        }
    }

    /**
     * 释放资源
     */
    public void release(){
        stopRecord();
        callback = null;
    }

    //----------------






}
