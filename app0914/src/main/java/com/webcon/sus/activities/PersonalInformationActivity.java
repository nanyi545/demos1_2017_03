package com.webcon.sus.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.webcon.sus.demo.R;
import com.webcon.sus.eventObjects.ModifyEvent;
import com.webcon.wp.utils.ApplicationManager;
import com.webcon.wp.utils.ModifyDialogUtil;
import com.webcon.wp.utils.WPApplication;

import de.greenrobot.event.EventBus;

public class PersonalInformationActivity extends BaseActivity implements
		OnClickListener {

	private Context mContext;
	private RelativeLayout userNameRL, passwordRL;
	private TextView title, userNameTxt, userIDTxt;
	private ImageButton back;

    @Override
    public void releaseHandler(){
    }


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_usermsg);
        EventBus.getDefault().register(this);
		mContext = this;
		initView();
	}

    private void initView() {
        title = (TextView) findViewById(R.id.title_tv);
        back = (ImageButton) findViewById(R.id.usermsg_back);
        userNameRL = (RelativeLayout) findViewById(R.id.usermsg_ll_username);
        passwordRL = (RelativeLayout) findViewById(R.id.usermsg_ll_password);
        userNameTxt = (TextView) findViewById(R.id.usermsg_txt_username);
        userIDTxt = (TextView) findViewById(R.id.usermsg_txt_userid);

        title.setText("个人信息");
        userIDTxt.setText(WPApplication.getInstance().getCurrentUser().getUserId());
        userNameTxt.setText(WPApplication.getInstance().getCurrentUser().getUserName());

        back.setOnClickListener(this);
        userNameRL.setOnClickListener(this);
        passwordRL.setOnClickListener(this);

        ApplicationManager.getInstance().addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        if (v == back) {
            PersonalInformationActivity.this.finish();
        } else if (v == userNameRL) {
            (new ModifyDialogUtil()).showModifyDialog(
                    mContext, ModifyEvent.MODIFY_EVENT_CHANGE_NAME, null).show();
        } else if (v == passwordRL) {
            (new ModifyDialogUtil()).showModifyDialog(
                    mContext, ModifyEvent.MODIFY_EVENT_CHANGE_PWD, null).show();
        }
    }

    public void onEventMainThread(ModifyEvent event){
        switch (event.getType()){
            // 昵称修改成功
            case ModifyEvent.MODIFY_EVENT_CHANGE_NAME:
				userNameTxt.setText(WPApplication.getInstance().getCurrentUser().getUserName());
                Toast.makeText(mContext, "您的用户昵称已成功修改！", Toast.LENGTH_SHORT).show();
                break;
            // 修改密码
            case ModifyEvent.MODIFY_EVENT_CHANGE_PWD:
                Toast.makeText(mContext, "您的登录密码已成功修改！", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

}
