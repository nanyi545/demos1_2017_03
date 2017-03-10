package test1.nh.com.demos1.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import pl.droidsonroids.gif.GifImageView;
import test1.nh.com.demos1.R;
import test1.nh.com.demos1.utils.images.BitmapUtil;

public class ImageActivity extends AppCompatActivity {


    public static void start(Context context) {
        final Intent intent = new Intent(context, ImageActivity.class);
        context.startActivity(intent);
    }


    ImageView iv1;
    GifImageView giv1;
    TextView tv1;

    ImageView compare1;
    ImageView compare2;

    ImageView offUI;



    // --> use of memory cache
    private LruCache<String, Bitmap> mMemoryCache;
    // --> put bitmaps into and retrieve bitmaps from the cache
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }



    // --> use of disk cache
//    private DiskLruCache mDiskLruCache;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initViews();
        initMemoryCache();



    }



    private void initMemoryCache() {
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory_kb = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.i("AAA","MAX memory in mb:"+maxMemory_kb/ 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory_kb / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }


    private void initViews() {
        // compare imageView and gifImageView
        iv1=(ImageView)findViewById(R.id.iv1_ivTest);
        iv1.setImageResource(R.drawable.g10);
        giv1=(GifImageView)findViewById(R.id.gifiv1_ivTest);
        giv1.setImageResource(R.drawable.g10);



        // read bitmap dimensions and type
        tv1=(TextView) findViewById(R.id.id_tv1);
        compare1=(ImageView) findViewById(R.id.iv_scale_compare1);
        compare2=(ImageView) findViewById(R.id.iv_scale_compare2);

        BitmapFactory.Options options = new BitmapFactory.Options();
        // Setting the inJustDecodeBounds property to true while decoding avoids memory allocation,
        // returning null for the bitmap object but setting outWidth, outHeight and outMimeType.
//         options.inJustDecodeBounds = true; //  true --> decode returns null   false --> decode returns bitmap
        Bitmap bitmap1=BitmapFactory.decodeResource(getResources(), R.drawable.twitter, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;  // imageType = "image/gif" "image/jpeg"  "image/png"
        String bitmap_info1="imageHeight="+imageHeight+"  imageWidth"+imageWidth+"  imageType="+imageType;
        compare1.setImageBitmap(bitmap1);



        // load again with sampling---load a smaller version into memory
        options.inSampleSize=8;
        // inSampleSize can be calculated by BitmapUtil.calculateInSampleSize()
        Bitmap bitmap2=BitmapFactory.decodeResource(getResources(), R.drawable.twitter, options);
        imageHeight = options.outHeight;
        imageWidth = options.outWidth;
        imageType = options.outMimeType;  // imageType = "image/gif" "image/jpeg"  "image/png"
        String bitmap_info2="imageHeight="+imageHeight+"  imageWidth"+imageWidth+"  imageType="+imageType;
        compare2.setImageBitmap(bitmap2);

        tv1.setText(bitmap_info1+"\n"+bitmap_info2);



        // The BitmapFactory.decode* methods,
        // should not be executed on the main UI thread if the source data is read from disk
        //            or a network location (or really any source other than memory)
        offUI=(ImageView)findViewById(R.id.load_off_ui);
        // on UI thread ---> not good
//        offUI.setImageBitmap(BitmapUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.basketbal,20,20));
        // load on work thread and display on UI thread---> good
        loadBitmap(R.drawable.basketbal,offUI);









    }




    public void loadBitmap(int resId, ImageView imageView) {
        BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        task.execute(resId);
    }



    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int data = 0;

        public BitmapWorkerTask(ImageView imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            data = params[0];
            return BitmapUtil.decodeSampledBitmapFromResource(getResources(), data,20,20);
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
//                Log.i("AAA","==??"+(imageView==offUI)+"equals??"+(imageView.equals(offUI)));// true--true--
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

}
