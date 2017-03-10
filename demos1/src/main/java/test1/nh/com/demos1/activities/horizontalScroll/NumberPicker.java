package test1.nh.com.demos1.activities.horizontalScroll;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v4.view.ViewConfigurationCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import test1.nh.com.demos1.R;

/**
 * Created by Administrator on 2016/9/26.
 */
public class NumberPicker extends View {
    //基本设置
    /**
     * picker宽度
     */
    private int mWidth;
    /**
     * picker高度
     */
    private int mHeight;


    /**
     * 用于修改最大滑动速度(比例)
     */
    private static final int SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT = 8;
    /**
     * 最小滑动速度
     */
    private int mMinimumFlingVelocity;
    /**
     * 最大滑动速度
     */
    private int mMaximumFlingVelocity;

    /**
     * 背景颜色
     */
    private int mBackgroundColor;
    /**
     * 默认背景颜色
     */
    private static final int DEFAULT_BACKGROUND_COLOR = Color.rgb(255, 255, 255);

    //数值设置
    /**
     * 起始值
     */
    private int mStartNumber;
    /**
     * 终值
     */
    private int mEndNumber;
    /**
     * 当前值
     */
    private int mCurrentNumber;
    /**
     * 数值数组，存取所有选项的值
     */
    private int[] mNumberArray;
    /**
     * 当前值index
     */
    private int mCurrNumIndex;

    //边条设置
    /**
     * 条边画笔
     */
    private TextPaint mTextPaintHighLight;
    /**
     * 默认条边颜色
     */
    private static final int DEFAULT_TEXT_COLOR_HIGH_LIGHT = Color.rgb(0, 150, 71);
    /**
     * 条边颜色
     */
    private int mTextColorHighLight;
    /**
     * 条边大小
     */
    private float mTextSizeHighLight;
    /**
     * 默认条边大小
     */
    private static final float DEFAULT_TEXT_SIZE_HIGH_LIGHT = 36;
    /**
     * 边条矩阵
     */
    private Rect mTextBoundsHighLight;
    /**
     * 边条画笔
     */
    private Paint mLinePaint;
    /**
     * 设置边条粗度
     */
    private static final int lineWidth = 4;

    //选项设置
    /**
     * 选项画笔
     */
    private TextPaint mTextPaintNormal;
    /**
     * 选项字体颜色
     */
    private int mTextColorNormal;
    /**
     * 默认选项字体颜色
     */
    private static final int DEFAULT_TEXT_COLOR_NORMAL = Color.rgb(0, 0, 0);
    /**
     * 选项字体大小
     */
    private float mTextSizeNormal;
    /**
     * 默认选项字体大小
     */
    private static final float DEFAULT_TEXT_SIZE_NORMAL = 32;
    /**
     * 两个选项之间的垂直距离
     */
    private int mVerticalSpacing;
    /**
     * 默认两个选项之间的垂直距离
     */
    private static final int DEFAULT_VERTICAL_SPACING = 16;
    /**
     * 选项文字矩阵
     */
    private Rect mTextBoundsNormal;
    /**
     * 每个picker每次显示多少选项=边条数目+1
     */
    private int mLines;
    /**
     * 默认选项数目=默认边条数目+1
     */
    private static final int DEFAULT_LINES = 3;

    //遮罩设置
    /**
     * 上遮罩画笔
     */
    private Paint mShaderPaintTop;
    /**
     * 下遮罩画笔
     */
    private Paint mShaderPaintBottom;

    //右上角文字设置
    /**
     * 高亮数字的右上角显示的文字
     */
    private String mFlagText;
    /**
     * 右上角文字颜色
     */
    private int mFlagTextColor;
    /**
     * 右上角文字大小
     */
    private float mFlagTextSize;
    /**
     * 默认右上角文字大小
     */
    private static final float DEFAULT_FLAG_TEXT_SIZE = 12;
    /**
     * 默认右上角文字颜色
     */
    private static final int DEFAULT_FLAG_TEXT_COLOR = Color.rgb(148, 148, 148);
    /**
     *右上角文字画笔
     */
    private TextPaint mTextPaintFlag;
    /**
     * 存储当前显示项
     * 长度为每次显示的选项数目+4
     */
    private NumberHolder[] mTextYAxisArray;
    /**
     * 起始绘制Y坐标=控件高度/2-3*选项高度
     */
    private int mStartYPos;
    /**
     * 起始结束Y坐标=控件高度/2+3*选项高度
     */
    private int mEndYPos;
    /**
     * 自定义选项数组
     * 除了数字以外，我们还可以传入字符串数组，从而显示字符串选项
     */
    private String[] mTextArray;
    /**
     * getScaledTouchSlop是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。如果小于这个距离就不触发移动控件
     */
    private int mTouchSlop;
    /**
     * 表示整个picker
     */
    private RectF mHighLightRect;
    private Rect mTextBoundsFlag;
    private int mScrollState = OnScrollListener.SCROLL_STATE_IDLE;
    private int mTouchAction = MotionEvent.ACTION_CANCEL;
    /**
     * 该scroller用于滚动
     */
    private Scroller mFlingScroller;
    /**
     * 该scroller用于保证选项位置正确
     */
    private Scroller mAdjustScroller;
    private int mStartY;
    private int mCurrY;
    private int mOffectY;
    private int mSpacing;
    private boolean mCanScroll;
    /**
     * 用跟踪触摸屏事件（flinging事件和其他gestures手势事件）的速率
     */
    private VelocityTracker mVelocityTracker;
    private OnScrollListener mOnScrollListener;
    private OnValueChangeListener mOnValueChangeListener;
    private float mDensity;

    public NumberPicker(Context context) {
        this(context, null);
    }

    public NumberPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDensity = getResources().getDisplayMetrics().density;
        readAttrs(context, attrs, defStyleAttr);
        init();
    }
    /**
     * 读取自定义属性值
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void readAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberPicker, defStyleAttr, 0);

        mTextColorHighLight = a.getColor(R.styleable.NumberPicker_textColorHighLight, DEFAULT_TEXT_COLOR_HIGH_LIGHT);
        mTextColorNormal = a.getColor(R.styleable.NumberPicker_textColorNormal, DEFAULT_TEXT_COLOR_NORMAL);
        mTextSizeHighLight = a.getDimension(R.styleable.NumberPicker_textSizeHighLight, DEFAULT_TEXT_SIZE_HIGH_LIGHT * mDensity);
        mTextSizeNormal = a.getDimension(R.styleable.NumberPicker_textSizeNormal, DEFAULT_TEXT_SIZE_NORMAL * mDensity);
        mStartNumber = a.getInteger(R.styleable.NumberPicker_startNumber, 0);
        mEndNumber = a.getInteger(R.styleable.NumberPicker_endNumber, 0);
        mCurrentNumber = a.getInteger(R.styleable.NumberPicker_currentNumber, 0);
        mVerticalSpacing = (int) a.getDimension(R.styleable.NumberPicker_verticalSpacing, DEFAULT_VERTICAL_SPACING * mDensity);

        mFlagText = a.getString(R.styleable.NumberPicker_flagText);
        mFlagTextColor = a.getColor(R.styleable.NumberPicker_flagTextColor, DEFAULT_FLAG_TEXT_COLOR);
        mFlagTextSize = a.getDimension(R.styleable.NumberPicker_flagTextSize, DEFAULT_FLAG_TEXT_SIZE * mDensity);

        mBackgroundColor = a.getColor(R.styleable.NumberPicker_backgroundColor, DEFAULT_BACKGROUND_COLOR);

        mLines = a.getInteger(R.styleable.NumberPicker_lines, DEFAULT_LINES);
    }

    /**
     * 初始化
     */
    private void init() {
        verifyNumber();
        initPaint();
        initRects();
        measureText();

        //Configuration包含的方法和常量是可用于UI 超时，大小和距离的设置
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity() / SELECTOR_MAX_FLING_VELOCITY_ADJUSTMENT;

        mFlingScroller = new Scroller(getContext(), null);
        //DecelerateInterpolator表示在动画开始的地方快然后慢
        mAdjustScroller = new Scroller(getContext(), new DecelerateInterpolator(2.5f));
    }

    /**
     * 检查当前起始值，终值是否合理
     * 生成数值数组
     */
    private void verifyNumber() {
        if (mStartNumber < 0 || mEndNumber < 0) {//小于0,抛出异常
            throw new IllegalArgumentException("number can not be negative");
        }
        if (mStartNumber > mEndNumber) {
            mEndNumber = mStartNumber;
        }
        if (mCurrentNumber < mStartNumber) {
            mCurrentNumber = mStartNumber;
        }
        if (mCurrentNumber > mEndNumber) {
            mCurrentNumber = mEndNumber;
        }

        mNumberArray = new int[mEndNumber - mStartNumber + 1];
        for (int i = 0; i < mNumberArray.length; i++) {//生成数值数组
            mNumberArray[i] = mStartNumber + i;
        }

        mCurrNumIndex = mCurrentNumber - mStartNumber;//获取当前值的index
        mTextYAxisArray = new NumberHolder[mLines + 4];
    }

    /**
     * 初始化各种画笔
     */
    private void initPaint() {
        mTextPaintHighLight = new TextPaint();
        mTextPaintHighLight.setTextSize(mTextSizeHighLight);
        mTextPaintHighLight.setColor(mTextColorHighLight);
        mTextPaintHighLight.setFlags(TextPaint.ANTI_ALIAS_FLAG);
        mTextPaintHighLight.setTextAlign(Paint.Align.CENTER);

        mTextPaintNormal = new TextPaint();
        mTextPaintNormal.setTextSize(mTextSizeNormal);
        mTextPaintNormal.setColor(mTextColorNormal);
        mTextPaintNormal.setFlags(TextPaint.ANTI_ALIAS_FLAG);
        mTextPaintNormal.setTextAlign(Paint.Align.CENTER);

        mTextPaintFlag = new TextPaint();
        mTextPaintFlag.setTextSize(mFlagTextSize);
        mTextPaintFlag.setColor(mFlagTextColor);
        mTextPaintFlag.setFlags(TextPaint.ANTI_ALIAS_FLAG);
        mTextPaintFlag.setTextAlign(Paint.Align.LEFT);

        mLinePaint = new Paint();
        mLinePaint.setColor(mTextColorHighLight);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(lineWidth * mDensity);

        mShaderPaintTop = new Paint();
        mShaderPaintBottom = new Paint();
    }

    /**
     * 初始化矩形
     */
    private void initRects() {
        mTextBoundsHighLight = new Rect();
        mTextBoundsNormal = new Rect();
        mTextBoundsFlag = new Rect();
    }

    /**
     * 测量文字边界
     */
    private void measureText() {
        /*
         * 保证不同长度的数值边界相同
         * 例如"2014" 到 "0000".
         */
        String text = String.valueOf(mEndNumber);
        int length = text.length();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append("0");
        }
        text = builder.toString();
        //会按严格按照Paint的样式，绘制出文字的边界,调用native层去测量
        mTextPaintHighLight.getTextBounds(text, 0, text.length(), mTextBoundsHighLight);
        mTextPaintNormal.getTextBounds(text, 0, text.length(), mTextBoundsNormal);

        if (mFlagText != null) {
            mTextPaintFlag.getTextBounds(mFlagText, 0, mFlagText.length(), mTextBoundsFlag);
        }
    }


    Camera camera=new Camera();
    Matrix matrix = new Matrix();

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制背景
        canvas.drawColor(mBackgroundColor);

        //绘制两条选中数上下的边条
        canvas.drawLine(0, mHighLightRect.top, mWidth, mHighLightRect.top, mLinePaint);
        canvas.drawLine(0, mHighLightRect.bottom, mWidth, mHighLightRect.bottom, mLinePaint);

        //绘制右上角文字
        if (mFlagText != null) {
            int x = (mWidth + mTextBoundsHighLight.width() + 6) / 2;
            canvas.drawText(mFlagText, x, mHeight / 2, mTextPaintFlag);
        }

        //绘制选项
        for (int i = 0; i < mTextYAxisArray.length; i++) {
            if (mTextYAxisArray[i].mmIndex >= 0 && mTextYAxisArray[i].mmIndex <= mEndNumber - mStartNumber) {
                String text = null;
                if (mTextArray != null) {//是否自定义字符数组
                    text = mTextArray[mTextYAxisArray[i].mmIndex];
                } else {
                    text = String.valueOf(mNumberArray[mTextYAxisArray[i].mmIndex]);
                }


//                canvas.save();
//                camera.save();
//                camera.translate(0, 0, 0);
//                camera.rotate((i-4)*10, 0, 0);
//                camera.getMatrix(matrix);
//                //绕(curX,curY)变换
//                matrix.preTranslate(-mWidth / 2, -(mTextYAxisArray[i].mmPos + mTextBoundsNormal.height() / 2));  // so that the rotation happens arround the point 50,50
//                matrix.postTranslate(mWidth / 2,mTextYAxisArray[i].mmPos + mTextBoundsNormal.height() / 2);
//                camera.restore();
//                canvas.concat(matrix);

                canvas.drawText(text, mWidth / 2, mTextYAxisArray[i].mmPos + mTextBoundsNormal.height() / 2, mTextPaintNormal);

//                canvas.restore();

            }
        }

        // 绘制遮罩
        canvas.drawRect(0, 0, mWidth, mHighLightRect.top, mShaderPaintTop);
        canvas.drawRect(0, mHighLightRect.bottom, mWidth, mHeight, mShaderPaintBottom);

        // Scroll the number to the position where exactly they should be.
        // Only do this when the finger no longer touch the screen and the fling action is finished.
        if (MotionEvent.ACTION_UP == mTouchAction && mFlingScroller.isFinished()) {
            adjustYPosition();
        }


//        Paint p=new Paint();
//        p.setColor(Color.rgb(120,0,0));
//        canvas.drawRect(mHighLightRect,p);  //  mHighLightRect  selected item rect in the middle...


    }

    /**
     * 使数字滑动正确的位置(也就是说选中项要在正中间)
     */
    private void adjustYPosition() {
        if (mAdjustScroller.isFinished()) {
            mStartY = 0;
            int offsetIndex = Math.round((float)(mTextYAxisArray[0].mmPos - mStartYPos) / (float)mSpacing);
            int position = mStartYPos + offsetIndex * mSpacing;
            int dy = position - mTextYAxisArray[0].mmPos;
            if (dy != 0) {
                mAdjustScroller.startScroll(0, 0, 0, dy, 300);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            // 父控件已经告诉picker要多宽
            mWidth = widthSize;
        } else {//否则
            //宽度=边条宽度+左内边距+右内边距+右上角文字宽度+6
            mWidth = mTextBoundsHighLight.width() + getPaddingLeft() + getPaddingRight() + mTextBoundsFlag.width() + 6;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            // 父控件已经告诉picker要多高
            mHeight = heightSize;
        } else {//否则
            //高度=选项数目*高度+边条数目*选项垂直距离+上内边距+下内边距
            mHeight = mLines * mTextBoundsNormal.height() + (mLines - 1) * mVerticalSpacing + getPaddingTop() + getPaddingBottom();
        }

        setMeasuredDimension(mWidth, mHeight);

        /*
         * Do some initialization work when the view got a size
         */
        if (null == mHighLightRect) {
            //RectF的坐标是用单精度浮点型表示的
            mHighLightRect = new RectF();//表示整个picker
            mHighLightRect.left = 0;
            mHighLightRect.right = mWidth;
            mHighLightRect.top = (mHeight - mTextBoundsHighLight.height() - mVerticalSpacing) / 2;
            mHighLightRect.bottom = (mHeight + mTextBoundsHighLight.height() + mVerticalSpacing) / 2;
            //上遮罩
            Shader topShader = new LinearGradient(0, 0, 0, mHighLightRect.top, new int[] {
                    mBackgroundColor & 0xDFFFFFFF,
                    mBackgroundColor & 0xCFFFFFFF,
                    mBackgroundColor & 0x00FFFFFF },
                    null, Shader.TileMode.CLAMP);
            //下遮罩
            Shader bottomShader = new LinearGradient(0, mHighLightRect.bottom, 0, mHeight, new int[] {
                    mBackgroundColor & 0x00FFFFFF,
                    mBackgroundColor & 0xCFFFFFFF,
                    mBackgroundColor & 0xDFFFFFFF },
                    null, Shader.TileMode.CLAMP);
            mShaderPaintTop.setShader(topShader);
            mShaderPaintBottom.setShader(bottomShader);
            //选项高度=垂直距离+文字高度
            mSpacing = mVerticalSpacing + mTextBoundsNormal.height();
            //起始绘制Y坐标=控件高度/2-3*选项高度
            mStartYPos = mHeight / 2 - 3 * mSpacing;
            //起始结束Y坐标=控件高度/2+3*选项高度
            mEndYPos = mHeight / 2 + 3 * mSpacing;

            initTextYAxisArray();
        }
    }

    /**
     * 初始化选项文字的Y坐标
     */
    private void initTextYAxisArray() {
        for (int i = 0; i < mTextYAxisArray.length; i++) {
            //保证选中项在正中间
            NumberHolder textYAxis = new NumberHolder(mCurrNumIndex - 3 + i, mStartYPos + i * mSpacing);
            if (textYAxis.mmIndex > mNumberArray.length - 1) {//如果选中项之后不够3项，用头部补足
                textYAxis.mmIndex -= mNumberArray.length;
            } else if (textYAxis.mmIndex < 0) {//如果选中项之前不够3项，用尾部补足
                textYAxis.mmIndex += mNumberArray.length;
            }
            mTextYAxisArray[i] = textYAxis;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {//是否enabled
            return false;
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        //表示用于多点 触控检测点
        int action = event.getActionMasked();
        mTouchAction = action;
        if (MotionEvent.ACTION_DOWN == action) {
            mStartY = (int) event.getY();
            if (!mFlingScroller.isFinished() || !mAdjustScroller.isFinished()) {//没有停止，强制停止
                mFlingScroller.forceFinished(true);
                mAdjustScroller.forceFinished(true);
                onScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
            }
        } else if (MotionEvent.ACTION_MOVE == action) {
            mCurrY = (int) event.getY();
            mOffectY = mCurrY - mStartY;

            if (!mCanScroll && Math.abs(mOffectY) < mTouchSlop) {
                return false;
            } else {
                mCanScroll = true;
                if (mOffectY > mTouchSlop) {
                    mOffectY -= mTouchSlop;
                } else if (mOffectY < -mTouchSlop) {
                    mOffectY += mTouchSlop;
                }
            }

            mStartY = mCurrY;

            computeYPos(mOffectY);

            onScrollStateChange(OnScrollListener.SCROLL_STATE_TOUCH_SCROLL);
            invalidate();
        } else if (MotionEvent.ACTION_UP == action) {
            mCanScroll = false;

            VelocityTracker velocityTracker = mVelocityTracker;
            velocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
            int initialVelocity = (int) velocityTracker.getYVelocity();

            if (Math.abs(initialVelocity) > mMinimumFlingVelocity) {//如果快速滑动
                fling(initialVelocity);
                onScrollStateChange(OnScrollListener.SCROLL_STATE_FLING);
            } else {//如果只是轻微移动
                adjustYPosition();
                invalidate();
            }
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }

        return true;
    }

    /**
     * 这个函数会在控件滚动的时候调用，准确来说，是在父控件调用drawchild()以后，在控件调用draw()以前
     */
    @Override
    public void computeScroll() {
        Scroller scroller = mFlingScroller;
        if (scroller.isFinished()) {//如果滚动已经停止了
            onScrollStateChange(OnScrollListener.SCROLL_STATE_IDLE);
            scroller = mAdjustScroller;//使scroller为mAdjustScroller
            if (scroller.isFinished()) {
                return;
            }
        }

        scroller.computeScrollOffset();

        mCurrY = scroller.getCurrY();
        mOffectY = mCurrY - mStartY;

        computeYPos(mOffectY);

        invalidate();
        mStartY = mCurrY;
    }

    /**
     * Make {@link #mTextYAxisArray} to be a circular array
     * 使mTextYAxisArray变成一个循环数组
     *
     * @param offectY
     */
    private void computeYPos(int offectY) {
        for (int i = 0; i < mTextYAxisArray.length; i++) {

            mTextYAxisArray[i].mmPos += offectY;
            if (mTextYAxisArray[i].mmPos >= mEndYPos + mSpacing) {//如果新位置超出高度
                mTextYAxisArray[i].mmPos -= (mLines + 2) * mSpacing;//定位到开头
                mTextYAxisArray[i].mmIndex -= (mLines + 2);//更新序号
                if (mTextYAxisArray[i].mmIndex < 0) {
                    mTextYAxisArray[i].mmIndex += mNumberArray.length;
                }
            } else if (mTextYAxisArray[i].mmPos <= mStartYPos - mSpacing) {//如果新位置超出起始点
                mTextYAxisArray[i].mmPos += (mLines + 2) * mSpacing;//定位到结尾
                mTextYAxisArray[i].mmIndex += (mLines + 2);//更新序号
                if (mTextYAxisArray[i].mmIndex > mNumberArray.length - 1) {
                    mTextYAxisArray[i].mmIndex -= mNumberArray.length;
                }
            }

            if (Math.abs(mTextYAxisArray[i].mmPos - mHeight / 2) < mSpacing / 4) {//离中间距离小于mSpacing / 4
                mCurrNumIndex = mTextYAxisArray[i].mmIndex;//取得当前值
                int oldNumber = mCurrentNumber;
                mCurrentNumber = mNumberArray[mCurrNumIndex];
                if (oldNumber != mCurrentNumber) {
                    if (mOnValueChangeListener != null) {
                        mOnValueChangeListener.onValueChange(this, oldNumber, mCurrentNumber);
                    }
                }
            }
        }
    }

    /**
     * 设置快速滑动fling
     * @param startYVelocity
     */
    private void fling(int startYVelocity) {
        int maxY = 0;
        if (startYVelocity > 0) {//向下滑动
            maxY = (int) (mTextSizeNormal * 10);
            mStartY = 0;
            mFlingScroller.fling(0, 0, 0, startYVelocity, 0, 0, 0, maxY);
        } else if (startYVelocity < 0) {//向上滑动
            maxY = (int) (mTextSizeNormal * 10);
            mStartY = maxY;
            mFlingScroller.fling(0, maxY, 0, startYVelocity, 0, 0, 0, maxY);
        }
        invalidate();
    }


    /**
     * 数字对象，保存有该数字所在的起始Y坐标，在当前显示项的index
     */
    class NumberHolder {
        /**
         * Array index of the number in {@link #mTextYAxisArray}
         */
        public int mmIndex;

        /**
         * 该数字的起始Y坐标
         */
        public int mmPos;

        public NumberHolder(int index, int position) {
            mmIndex = index;
            mmPos = position;
        }
    }

    public void setStartNumber(int startNumber) {
        mStartNumber = startNumber;
        verifyNumber();
        initTextYAxisArray();
        invalidate();
    }

    public void setEndNumber(int endNumber) {
        mEndNumber = endNumber;
        verifyNumber();
        initTextYAxisArray();
        invalidate();
    }

    public void setCurrentNumber(int currentNumber) {
        mCurrentNumber = currentNumber;
        verifyNumber();
        initTextYAxisArray();
        invalidate();
    }

    public int getCurrentNumber() {
        return mCurrentNumber;
    }

    /**
     * Interface to listen for changes of the current value.
     * 值改变监听接口
     */
    public interface OnValueChangeListener {

        /**
         * Called upon a change of the current value.
         *
         * @param picker The NumberPicker associated with this listener.
         * @param oldVal The previous value.
         * @param newVal The new value.
         */
        void onValueChange(NumberPicker picker, int oldVal, int newVal);
    }

    /**
     * Interface to listen for the picker scroll state.
     * 滑动监听接口
     */
    public interface OnScrollListener {
        /**
         * The view is not scrolling.
         * 没有滑动
         */
        public static int SCROLL_STATE_IDLE = 0;

        /**
         * The user is scrolling using touch, and their finger is still on the screen.
         * 因手指触摸而滑动
         */
        public static int SCROLL_STATE_TOUCH_SCROLL = 1;

        /**
         * The user had previously been scrolling using touch and performed a fling.
         * 手指已经离开屏幕时，继续滑动
         */
        public static int SCROLL_STATE_FLING = 2;

        /**
         * Callback invoked while the number picker scroll state has changed.
         *
         * @param scrollState The current scroll state. One of
         *            {@link #SCROLL_STATE_IDLE},
         *            {@link #SCROLL_STATE_TOUCH_SCROLL} or
         *            {@link #SCROLL_STATE_IDLE}.
         */
        public void onScrollStateChange(NumberPicker picker, int scrollState);
    }

    /**
     * 设置滑动监听
     * @param l
     */
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }
    /**
     * 设置值改变监听
     * @param l
     */
    public void setOnValueChangeListener(OnValueChangeListener l) {
        mOnValueChangeListener = l;
    }

    /**
     * Handles transition to a given <code>scrollState</code>
     * 改变滑动状态，并且通知监听器
     */
    private void onScrollStateChange(int scrollState) {
        if (mScrollState == scrollState) {
            return;
        }
        mScrollState = scrollState;
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChange(this, scrollState);
        }
    }



    /**
     * Display custom text array instead of numbers
     * 使用自定义的数组来展示选项
     * @param textArray
     */
    public void setCustomTextArray(String[] textArray) {
        mTextArray = textArray;
        invalidate();
    }

}
