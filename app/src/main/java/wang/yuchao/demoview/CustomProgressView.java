package wang.yuchao.demoview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author      : wangyuchao
 * DateTime    : 2018/6/26 14:25
 * Description :
 */
public class CustomProgressView extends View {

    private PathMeasure mPathMeasure;
    private Paint mPaint;
    private Path mPath = new Path();
    private float mLength;
    private float mAnimatorValue;

    public CustomProgressView(Context context) {
        super(context);
        this.initView(context);
    }

    public CustomProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    public CustomProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context);
    }

    private void initView(Context context) {
        mPathMeasure = new PathMeasure();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);

        Path path = new Path();
        path.addCircle(150, 150, 100, Path.Direction.CW);
        mPathMeasure.setPath(path, true);
        mLength = mPathMeasure.getLength();

        // 动画计算器
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(3 * 1000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath.reset();
        mPath.lineTo(0, 0);// 硬件加速的BUG

        float stop = mLength * mAnimatorValue;
        mPathMeasure.getSegment(0, stop, mPath, true);

        canvas.drawPath(mPath, mPaint);
    }
}
