package wang.yuchao.demoview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Author      : wangyuchao
 * DateTime    : 2018/6/26 15:08
 * Description :
 */
public class SyncTextPathView extends View {

    // 静态变量都是可以 set/get 的属性，这里全部写成固定参数
    protected static final String TEXT = "王玉超Hello!";
    protected static final int DURATION = 6 * 1000;

    protected Paint mDrawPaint;     // 路径画笔
    protected TextPaint mTextPaint; // 文字画笔

    protected Path mDrawPath = new Path();    // 绘画路径
    protected Path mTextPath = new Path();    // 文字路径

    protected float mAnimatorValue;

    protected PathMeasure mPathMeasure = new PathMeasure();

    protected float mStop;
    protected float mLengthSum;

    public SyncTextPathView(Context context) {
        super(context);
        this.initView(context);
    }

    public SyncTextPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public SyncTextPathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    private void initView(Context context) {
        initPaint();

        initAnimator();

        initData();
    }

    private void initPaint() {
        mDrawPaint = new Paint();
        mDrawPaint.setAntiAlias(true);
        mDrawPaint.setColor(Color.BLACK);
        mDrawPaint.setStrokeWidth(5);
        mDrawPaint.setStyle(Paint.Style.STROKE);
        mDrawPaint.setTextAlign(Paint.Align.CENTER);

        mTextPaint = new TextPaint();
        mTextPaint.setTextSize(128);
    }

    private void initAnimator() {
        ValueAnimator mAnimator = ValueAnimator.ofFloat(0, 1);

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        mAnimator.removeAllListeners();

        mAnimator.setDuration(DURATION);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);

        mAnimator.start();
    }

    private void initData() {
        mTextPaint.getTextPath(TEXT, 0, TEXT.length(), 50, 250, mTextPath);
        mPathMeasure.setPath(mTextPath, true);

        mLengthSum = mPathMeasure.getLength();
        while (mPathMeasure.nextContour()) {
            mLengthSum = mLengthSum + mPathMeasure.getLength();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 重置路径
        mPathMeasure.setPath(mTextPath, true);
        // 重置
        mDrawPath.reset();
        mDrawPath.lineTo(0, 0);// 硬件加速的BUG
        // 要绘制到的位置
        float stop = mLengthSum * mAnimatorValue;

        // 循环绘制片段
        while (stop > mPathMeasure.getLength()) {
            stop = stop - mPathMeasure.getLength();
            mPathMeasure.getSegment(0, mPathMeasure.getLength(), mDrawPath, true); // 绘制片段
            mPathMeasure.nextContour();
        }
        mPathMeasure.getSegment(0, stop, mDrawPath, true);// 绘制最后不到一个片段

        // 绘制路径
        canvas.drawPath(mDrawPath, mDrawPaint);
    }
}
