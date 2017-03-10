package com.webcon.sus.receiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.webcon.sus.demo.R;
import com.webcon.sus.service.CService;
import com.webcon.sus.utils.SUConstant;
import com.webcon.wp.utils.ApplicationManager;
import com.webcon.wp.utils.NotificationCancelManager;
import com.webcon.wp.utils.WPApplication;

/**
 * 程序异常回调的广播
 * //FIXME: 需要详细查看的异常处理
 * @author Vieboo
 * 
 */
public class LogoutExitReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.dialog_logoutexitreceiver_title);

		if (intent.getExtras().getInt("msgType") == SUConstant.CALLBACK_PDU_MSG_TYPE_OnSecondLogined) {
			builder.setMessage(R.string.dialog_logoutexitreceiver_messagebe);
		} else {
            builder.setMessage(R.string.dialog_logoutexitreceiver_messageself);
        }

		//------clear alarm notifications-------
		NotificationCancelManager.getInstance().clearAllNotification(context);

		builder.setCancelable(false);
		builder.setPositiveButton(R.string.dialog_logoutexitreceiver_button,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
                        //一些清理操作
						WPApplication.flag_logout = false;
						ApplicationManager.getInstance().applicationExit();
						ApplicationManager.getInstance().applicationOtherExit();
                        //停止后台服务
						context.stopService(new Intent(context, CService.class));
					}
				});
		builder.setOnKeyListener(new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                boolean ret = false;
				if (keyCode == KeyEvent.KEYCODE_SEARCH) {
					ret = true;
				}
				return ret;
			}
		});
		AlertDialog alertDialog = builder.create();
		alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alertDialog.show();
	}

}
