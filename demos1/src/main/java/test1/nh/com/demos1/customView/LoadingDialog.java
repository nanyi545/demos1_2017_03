package test1.nh.com.demos1.customView;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import test1.nh.com.demos1.R;

/**
 * Created by Administrator on 16-9-8.
 */
public class LoadingDialog extends Dialog {



    private Dialog mpd = null;
    private LayoutInflater lyt_Inflater = null;
    LoadView loadView;


    public LoadingDialog(Context context) {
        super(context);
        initDialog(context,true);
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initDialog(context,cancelable);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        initDialog(context,true);
    }


    public void initDialog(Context context, boolean cancelable)
    {
        lyt_Inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view_lyt = lyt_Inflater.inflate(     R.layout.layout_load_indicator, null);


        loadView= (LoadView) view_lyt.findViewById(R.id.loadview);


        mpd = new Dialog(context, R.style.ThemeDialogCustom);
        mpd.setContentView(view_lyt);
        mpd.setCancelable(cancelable);
    }

    public void start (){

        mpd.show();
        loadView.setProgress(1);

    }




}
