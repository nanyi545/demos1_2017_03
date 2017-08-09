package test1.nh.com.demos1.audioTest;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by Administrator on 15-9-23.
 */
public class AudioUtils2 {
    public static final String TAG = "AudioUtils2";
    /** 本地音频文件保存路径 */
    public static final String FILEPATH = Environment.getExternalStorageDirectory().toString() +
            File.separator + "AudioTest" + File.separator;
    /** 本地保存音频文件名 */
    public static final String WAV_FILE=FILEPATH+"soundInBytes.pcm";
    public static final String WAV_FILE1 = FILEPATH + "soundInString_nonCoded.txt";
    public static final String WAV_FILE2 = FILEPATH + "soundInString_Coded.txt";

    /** 音频获取源设备 */
    public static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
    /** 设置音频采样率 */
    public static final int SAMEPLE_RATE_HZ = 4000;   //   min 4000...       original ..  16000Hz
    /** 设置音频录制的声道   **CHANNEL_IN_STEREO为双声道，CHANNEL_IN_MONO为单声道*/
    public static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO;

    /** 音频数据格式  **PCM 16位每个样本 */
    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;   //ENCODING_PCM_16BIT
    /** 音频流的类型 */
    public static final int STREAM_TYPE = AudioManager.STREAM_MUSIC;
    /** Track模式 */
    public static final int STREAM_MODE = AudioTrack.MODE_STREAM;
    /** 输出比特率 */
    public static final int BITRATE = 32000;           // 24k(包长60)：9-15    36800---包长92
    /** 录音缓冲区大小 */
    public static final int RECORD_BUFFER_SIZE  = 640;  // 原来640 ----9-18    both--92-->works
    /** 读取缓冲区大小 */
    public static final int TRACK_BUFFER_SIZE   = 640;  //  原来60

    /** 应用于本地 */
    public static final int APPLY_TYPE_LOCAL = 1;
    /** 应用于网络 */
    public static final int APPLY_TYPE_ONLINE = 2;

    /** 音频数据回调接口 */
    private IRecordData callback;
    /** 音频录制工具 */
    private AudioRecorder2 mRecorder;
    /** 音频播放工具 */
    private AudioPlayer2 mPlayer;


    public AudioUtils2(IRecordData callback) {
        this.callback = callback;
//        Log.i("BBB","--"+callback.toString());  // initialization success?
        initDir();
    }

    /**
     * 初始化文件目录
     */
    private void initDir() {
        File dir = new File(FILEPATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Log.i("bbb","file path:"+FILEPATH);
    }

        /* ------------ public --------------- */

    /**
     * 检查音频文件是否存在
     */
    public boolean checkFile(){
        File file = new File(WAV_FILE);
        return file.exists();
    }

    /**
     * 清除已存在的音频文件
     */
    public void clearFile(){
        File file = new File(WAV_FILE);
        if(file.exists()){
            file.delete();
        }
    }

    /**
     * 开始录音
     */
    public void startRecord() {
        stopRecord();   //清空语音队列
        mRecorder = new AudioRecorder2(callback);
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
     * 播放录音
     */
    public void startPlay() {
        stopPlay();
        mPlayer = new AudioPlayer2(callback);
        mPlayer.start();
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        if(mPlayer != null){
            mPlayer.stop();
            mPlayer = null;
        }
    }

    /**
     * 释放资源
     */
    public void release(){
        stopPlay();
        stopRecord();
        callback = null;
    }

    //----------------






}
