package com.sqisland.android.animated_vector_drawable;

import android.app.Activity;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class DowloadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dowload);

        ImageView mIcDownloadAnimator = (ImageView) findViewById(R.id.ic_download_iv);
        Drawable drawable = mIcDownloadAnimator.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }


    }
}
