package com.webcon.sus.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.webcon.sus.demo.R;
import com.webcon.sus.eventObjects.AudioEvent;
import com.webcon.sus.utils.AudioUtils;
import com.webcon.wp.utils.ApplicationManager;

import de.greenrobot.event.EventBus;

/**
 * @author m
 */
public class ExpelAudioActivity extends BaseActivity implements View.OnClickListener{

    private final static int LIMITED_TIME = 60 * 1000 * 5;//录音时间限制
    private TextView tv_head;
    private Button bn_record, bn_play, bn_clear;
    private ImageButton bn_back;

    private AudioUtils mAudio;
    private boolean isWorking = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView(){
        setContentView(R.layout.layout_expel_audio);
        bn_record = (Button)findViewById(R.id.bn_voice_record_switch);
        bn_play = (Button)findViewById(R.id.bn_voice_play_switch);
        bn_clear = (Button)findViewById(R.id.bn_voice_clear);
        bn_back = (ImageButton) findViewById(R.id.usermsg_back);

        bn_record.setOnClickListener(this);
        bn_play.setOnClickListener(this);
        bn_clear.setOnClickListener(this);
        bn_back.setOnClickListener(this);

        tv_head = (TextView)findViewById(R.id.title_tv);
        tv_head.setText("警告语音预录制");

        mAudio = new AudioUtils(null);
        refreshButton(mAudio.checkFile());

        ApplicationManager.getInstance().addOtherActivity(this);
    }

    private void refreshButton(boolean b){
        bn_play.setEnabled(b);
        bn_clear.setEnabled(b);
    }

    @Override
    public void onClick(View v){
        if(bn_record == v){
            if(isWorking){
                //停止录制
                bn_record.setText("开始录制");
                refreshButton(true);
                mAudio.stopRecord();
            }else{
                //开始录制
                refreshButton(false);
                bn_record.setText("停止录制");


                try {
                    mAudio.startRecord();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("audio","单双声道问题？？");
                }


            }
            isWorking = !isWorking;
        }

        if(bn_play == v){
            if(isWorking){
                bn_record.setEnabled(true);
                bn_clear.setEnabled(true);
                bn_play.setText("开始播放");
                mAudio.stopPlay();
            }else{
                bn_record.setEnabled(false);
                bn_clear.setEnabled(false);
                bn_play.setText("停止播放");
                mAudio.startPlay();
            }
            isWorking = !isWorking;
        }

        if(bn_clear == v){
            mAudio.clearFile();
            refreshButton(false);
        }
        if(bn_back == v){
            finish();
        }
    }

    @Override
    public void releaseHandler(){

    }

    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    public void onDestroy(){
        EventBus.getDefault().unregister(this);
        mAudio.release();
        super.onDestroy();
    }

    public void onEventMainThread(AudioEvent event){
        if(event.getType() == AudioEvent.AUDIO_EVENT_PLAY_COMPLETE){
            if(isWorking){
                bn_record.setEnabled(true);
                bn_clear.setEnabled(true);
                bn_play.setText("开始播放");
                mAudio.stopPlay();
                isWorking = !isWorking;
            }
        }
    }


    //----------------------------------------------//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_expel_audio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
