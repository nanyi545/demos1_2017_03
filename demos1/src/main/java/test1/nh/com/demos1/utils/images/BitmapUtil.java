package test1.nh.com.demos1.utils.images;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.util.Log;
import android.widget.ImageView;

/**
 * @author ww
 */
public class BitmapUtil {

    /**
     * returns the scaled bitmap based on the requested dimension of image
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /**
     * returns the calculateInSampleSize to Load a Scaled Down Version of bitmap
     * based on the  requested width and height of the image
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        Log.i("InSampleSize",""+inSampleSize); // check the inSampleSize in logcat
        return inSampleSize;
    }


    /**
     * 获取正确 缩放&裁剪 适应IamgeView的Bitmap
     */
    public static Bitmap createFitBitmap(ImageView imageView, Bitmap bitmap) {
        return createFitBitmap(imageView.getWidth(), imageView.getHeight(), bitmap);
    }

    /**
     * 根据目的长宽 缩放&裁剪 Bitmap
     * @param destWidth 需要的长度
     * @param destHeight 需要的宽度
     * @param bitmap 待处理图片
     * @return 修改完成的图片
     */
    public static Bitmap createFitBitmap(int destWidth, int destHeight, Bitmap bitmap){

        int orignWidth = bitmap.getWidth();
        int orignHeight = bitmap.getHeight();
        Bitmap result = null;
        if (orignWidth >= destWidth && orignHeight >= destHeight) {
            result = createLargeToSmallBitmap(
                    orignWidth, orignHeight, destWidth, destHeight, bitmap);
        } else if (orignWidth >= destWidth && orignHeight < destHeight) {
            Bitmap temp = createLargeWidthToEqualHeightBitmap(
                    orignWidth, orignHeight, destWidth, destHeight, bitmap);
            result = createLargeToSmallBitmap(
                    temp.getWidth(), temp.getHeight(), destWidth, destHeight, temp);
        } else if (orignWidth < destWidth && orignHeight >= destHeight) {
            Bitmap temp = createLargeHeightToEqualWidthBitmap(
                    orignWidth, orignHeight, destWidth, destHeight, bitmap);
            result = createLargeToSmallBitmap(
                    temp.getWidth(), temp.getHeight(), destWidth, destHeight, temp);
        } else {
            Bitmap temp = createSmallToEqualBitmap(
                    orignWidth, orignHeight, destWidth, destHeight, bitmap);
            result = createFitBitmap(destWidth, destHeight, temp);
        }

        return result;
    }

    /**
     * 获取缩放的图片
     */
    public static Bitmap createScaledBitmap(int destWidth, int destHeight, Bitmap bitmap){
        int orignWidth = bitmap.getWidth();
        int orignHeight = bitmap.getHeight();
        Bitmap result = null;
        if (orignWidth >= destWidth && orignHeight >= destHeight) {
            result = createLargeToEqualBitmap(
                    orignWidth, orignHeight, destWidth, destHeight, bitmap);
        } else if (orignWidth >= destWidth && orignHeight < destHeight) {
            result = createLargeWidthToEqualHeightBitmap(
                    orignWidth, orignHeight, destWidth, destHeight, bitmap);
        } else if (orignWidth < destWidth && orignHeight >= destHeight) {
            result = createLargeHeightToEqualWidthBitmap(
                    orignWidth, orignHeight, destWidth, destHeight, bitmap);
        } else {
            result = createSmallToEqualBitmap(
                    orignWidth, orignHeight, destWidth, destHeight, bitmap);
        }
        return result;
    }


    /*  -----------  */
    private static Bitmap createLargeToSmallBitmap(
            int widthBitmap, int heightBitmap, int widthTarget, int heightTarget, Bitmap bitmap) {

        int x = (widthBitmap - widthTarget) / 2;
        int y = (heightBitmap - heightTarget) / 2;
        return Bitmap.createBitmap(bitmap, x, y, widthTarget, heightTarget);
    }

    private static Bitmap createLargeWidthToEqualHeightBitmap(
            int widthBitmap, int heightBitmap, int widthTarget, int heightTarget, Bitmap bitmap) {

        double scale = (heightTarget * 1.0) / heightBitmap;
        return Bitmap.createScaledBitmap(bitmap, (int) (widthBitmap * scale), heightTarget, false);
    }

    private static Bitmap createLargeHeightToEqualWidthBitmap(
            int widthBitmap, int heightBitmap, int widthTarget, int heightTarget, Bitmap bitmap) {

        double scale = (widthTarget * 1.0) / widthBitmap;
        return Bitmap.createScaledBitmap(bitmap, widthTarget, (int) (heightTarget * scale), false);
    }

    private static Bitmap createLargeToEqualBitmap(
            int widthBitmap, int heightBitmap, int widthTarget, int heightTarget, Bitmap bitmap){

        return createSmallToEqualBitmap(widthBitmap, heightBitmap, widthTarget, heightTarget, bitmap);
    }

    private static Bitmap createSmallToEqualBitmap(
            int widthBitmap, int heightBitmap, int widthTarget, int heightTarget, Bitmap bitmap) {

        double scaleWidth = (widthTarget * 1.0) / widthBitmap;
        double scaleHeight = (heightTarget * 1.0) / heightBitmap;
        double scale = Math.min(scaleWidth, scaleHeight);
        return Bitmap.createScaledBitmap(bitmap, (int) (widthBitmap * scale), (int) (heightBitmap * scale), false);
    }

//* -------------------

    /**
     * 覆盖图片颜色
     */
    public static Bitmap overlapBitmapColor(Bitmap bitmap, int color){
        Bitmap bmp = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bmp);
        canvas.drawColor(color, PorterDuff.Mode.SRC_IN);
        bitmap.recycle();
        return bmp;
    }

}
