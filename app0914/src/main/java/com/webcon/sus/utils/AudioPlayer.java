package com.webcon.sus.utils;

import android.media.AudioTrack;
import android.util.Log;

import com.webcon.g722.TransferDecoding;
import com.webcon.sus.develop_test.audioTest.AudioUtils2;
import com.webcon.sus.eventObjects.AudioEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import de.greenrobot.event.EventBus;
import vieboo.test.record.utils.IRecordData;

/**
 * 录音播放工具
 * @author m
 */
public class AudioPlayer {
    private static final String TAG = "AudioPlayer";
    //------------------------\
    private File mFile;
    private FileInputStream fis;

    private Writer out;   //字符流文件--写入解码后声音文件

    private final int readSize = AudioUtils.TRACK_BUFFER_SIZE;
    private byte[] buffer;

    private boolean isPlaying = false;
    private int applyType;
    private IRecordData callback;
    private TransferDecoding mDecoder;
    private AudioTrack mTrack;

    //------------------------
    public AudioPlayer(IRecordData callback){
        if(callback == null){
            applyType = AudioUtils.APPLY_TYPE_LOCAL;
        }else{
            this.callback = callback;
            applyType = AudioUtils.APPLY_TYPE_ONLINE;
        }
        init();
    }

    private void init(){
        buffer = new byte[readSize];
        checkFile();
        initDecoder();
    }

    private void initDecoder(){
        mDecoder = new TransferDecoding();
        mDecoder.g722_decoder_init(AudioUtils.BITRATE, AudioUtils.SAMEPLE_RATE_HZ);

        //录音播放缓冲区
        int trackBufferSize = AudioTrack.getMinBufferSize(
                AudioUtils.SAMEPLE_RATE_HZ, AudioUtils.CHANNEL_CONFIG, AudioUtils.AUDIO_FORMAT);

        //实例化AudioTrack对象
        mTrack = new AudioTrack(AudioUtils.STREAM_TYPE, AudioUtils.SAMEPLE_RATE_HZ,
                AudioUtils.CHANNEL_CONFIG, AudioUtils.AUDIO_FORMAT, trackBufferSize, AudioUtils.STREAM_MODE);
    }

    private void checkFile(){
        mFile = new File(AudioUtils.WAV_FILE);
        if(!mFile.exists() || mFile.length() <= 0){
            return;
        }
        try {
            fis = new FileInputStream(mFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // file to write sound in strings
        File file_s = new File(AudioUtils2.WAV_FILE2);
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

    public void start(){
        if(isPlaying){
            return;
        }
        isPlaying = true;
        new PlayThread().start();
    }

    public void stop(){
        isPlaying = false;
    }

    private void release(){
        mTrack.stop();
        mTrack.release();
        mTrack = null;
        mDecoder.g722_decoder_release();
        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(new AudioEvent(AudioEvent.AUDIO_EVENT_PLAY_COMPLETE));
    }

    private void execute(byte[] buffer, int length){
        switch (applyType){
            case AudioUtils.APPLY_TYPE_LOCAL:
                //播放原始数据
//                mTrack.write(buffer, 0, length);

                //播放编码数据
                byte[] playBuffer = new byte[length];	//原始数据
                System.arraycopy(buffer, 0, playBuffer, 0, length);
                byte[] finalData = new byte[AudioUtils.RECORD_BUFFER_SIZE]; //用于播放的数据

                //解码   public native int g722_decoder(byte[] orifinnalData, byte[] decodingData, int length);
                if(mDecoder.g722_decoder(finalData, playBuffer, length) > 0){  // 之前的  decode写反了？？
//                if(mDecoder.g722_decoder(playBuffer, finalData, length) > 0){
                    mTrack.write(finalData, 0, finalData.length);
//                    // 音频解码后  写入file
//                    try {
//                        out.write(getChars(finalData));
//                        out.flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }


//                     //-------------测试                   // 音频解码后  写入file
//                    times=times+1;
//                    try {
//                        out.write("*"+times+"\n");
//                        out.flush();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }
                break;
            case AudioUtils.APPLY_TYPE_ONLINE:
                callback.onRecordData(buffer, length);
                break;
            default:
                break;
        }

    }


    // 测试函数
    private void execute1(byte[] buffer, int length){
        switch (applyType){
            case AudioUtils.APPLY_TYPE_LOCAL:

                //播放编码数据
                byte[] playBuffer = new byte[length];	//原始数据
                System.arraycopy(buffer, 0, playBuffer, 0, length);
                byte[] finalData = new byte[AudioUtils.RECORD_BUFFER_SIZE]; //用于播放的数据

                //解码
                if(mDecoder.g722_decoder(finalData, playBuffer, length) > 0){

                    // 音频解码后  写入file
                    try {
                        out.write(getChars(buffer));
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case AudioUtils.APPLY_TYPE_ONLINE:
                callback.onRecordData(buffer, length);
                break;
            default:
                break;
        }

    }


    //测试用变量
    int times=0;

    //---------------------------
    /**
     * 解码播放线程
     *
     */
    class PlayThread extends Thread{

        @Override
        public void run(){
            mTrack.play();
            int length = 0;
            //开始播放
            try {
                while(isPlaying){
                    length = fis.read(buffer,0,readSize);
                    if(length < 0){
                        isPlaying = false;
                        break;
                    }
                    Log.i("G722","length--play"+length);
                    execute(buffer, length);  //播放
//                    execute1(buffer, length);  //写入 soundInString_Coded.txt
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                release();
            }
        }
    }


    private String getChars (byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(int ii=0;ii<bytes.length;ii=ii+1){
            sb.append(bytes[ii]+"\n");
        }
        return sb.toString();
    }

}
