package test1.nh.com.demos1.activities.time_picker;

import android.app.Activity;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;


/**
 * Created by Administrator on 2016/7/19.
 */
public class Rotate3dAnimation extends Animation {
    private final float mFromDegrees;
    private final float mToDegrees;
    private final float mCenterX;
    private final float mCenterY;
    private final float mDepthZ;
    private final boolean mReverse;
    private Camera mCamera;

    /**
     * Creates a new 3D rotation on the Y axis. The rotation is defined by its
     * start angle and its end angle. Both angles are in degrees. The rotation
     * is performed around a center point on the 2D space, definied by a pair
     * of X and Y coordinates, called centerX and centerY. When the animation
     * starts, a translation on the Z axis (depth) is performed. The length
     * of the translation can be specified, as well as whether the translation
     * should be reversed in time.
     *
     * @param fromDegrees the start angle of the 3D rotation
     * @param toDegrees the end angle of the 3D rotation
     * @param centerX the X center of the 3D rotation
     * @param centerY the Y center of the 3D rotation
     * @param reverse true if the translation should be reversed, false otherwise
     */
    public Rotate3dAnimation(float fromDegrees, float toDegrees,
                             float centerX, float centerY, float depthZ, boolean reverse) {
        mFromDegrees = fromDegrees;
        mToDegrees = toDegrees;
        mCenterX = centerX;
        mCenterY = centerY;
        mDepthZ = depthZ;
        mReverse = reverse;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mCamera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final float fromDegrees = mFromDegrees;
        float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);

        final float centerX = mCenterX;
        final float centerY = mCenterY;
        final Camera camera = mCamera;

        final Matrix matrix = t.getMatrix();

        Log.i("interpolatedTime", interpolatedTime+"");
        camera.save();
        if (mReverse) {
            camera.translate(0.0f, 0.0f, mDepthZ * interpolatedTime);
        } else {
            camera.translate(0.0f, 0.0f, mDepthZ * (1.0f - interpolatedTime));
        }
        camera.rotateX(degrees);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }




    private static  Rotate3dAnimation initOpenAnim(int depthZ,int endAngle,int centerX,int centerY) {
        //从0到90度，顺时针旋转视图，此时reverse参数为true，达到90度时动画结束时视图变得不可见，
        Rotate3dAnimation openAnimation = new Rotate3dAnimation(0, endAngle, centerX, centerY, depthZ,true);
        openAnimation.setDuration(1000);
        openAnimation.setFillAfter(true);
        openAnimation.setInterpolator(new AccelerateInterpolator());
        return openAnimation;
    }

    public static void animationTextView(Activity activity, int tVid, int depthZ, int endAngle){
        TextView test_tv= (TextView) activity.findViewById(tVid);
        int centerX = test_tv.getWidth()/2;
        int centerY = test_tv.getHeight();
        test_tv.startAnimation(initOpenAnim(depthZ,endAngle,centerX,centerY));
    }

    public static void animationTextView(Activity activity, TextView test_tv, int depthZ, int endAngle){
        int centerX = test_tv.getWidth()/2;
        int centerY = test_tv.getHeight();
        test_tv.startAnimation(initOpenAnim(depthZ,endAngle,centerX,centerY));
    }



    public static void animationTextView2(Activity activity, int tVid, int depthZ, int endAngle){
        TextView test_tv= (TextView) activity.findViewById(tVid);
        int centerX = test_tv.getWidth()/2;
        int centerY = 0;
        test_tv.startAnimation(initOpenAnim(depthZ,endAngle,centerX,centerY));
    }


    public static void animationTextView2(Activity activity, TextView test_tv, int depthZ, int endAngle){
        int centerX = test_tv.getWidth()/2;
        int centerY = 0;
        test_tv.startAnimation(initOpenAnim(depthZ,endAngle,centerX,centerY));
    }



}
