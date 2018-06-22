package wang.yuchao.demoview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Author      : wangyuchao
 * DateTime    : 2018/6/22 14:21
 * Description :
 */
public class SquareImageView extends ImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();

        if (mWidth > mHeight) {
            mWidth = mHeight;
        } else {
            mHeight = mWidth;
        }

        setMeasuredDimension(mWidth, mHeight);
    }
}
