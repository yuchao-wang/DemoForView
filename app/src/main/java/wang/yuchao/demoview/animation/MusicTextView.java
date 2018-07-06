package wang.yuchao.demoview.animation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * Author      : wangyuchao
 * DateTime    : 2018/7/5 18:22
 * Description : 歌词渐变 View
 */
public class MusicTextView extends View {

    private static final String TAG = "music";

    private static final String TEXT = "王玉超 Hello World";

    private Rect bounds = new Rect();
    private Paint backPaint = new Paint();
    private Paint forePaint = new Paint();

    // 进度 0 - 100
    private float progress;
    private Handler handler = new Handler();

    public MusicTextView(Context context) {
        super(context);
        this.init(context);
    }

    public MusicTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context);
    }

    public MusicTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        backPaint.getTextBounds(TEXT, 0, TEXT.length(), bounds);
        setMeasuredDimension(bounds.width(), bounds.height());
    }

    private void init(Context context) {
        // Color.BLACK
        backPaint.setColor(Color.parseColor("#000000"));
        forePaint.setStyle(Paint.Style.FILL);
        backPaint.setTextSize(50);

        forePaint.setColor(Color.parseColor("#FF0000"));
        forePaint.setStyle(Paint.Style.FILL);
        forePaint.setTextSize(50);
    }

//    // 文本宽度获取方式一：包括字体间间隙,因此更大点
//    width = paint.measureText(text,0,text.length());
//    // 文本宽度获取方式二：不包括字体间隙,真实宽高
//    Rect rect = new Rect();
//    paint.getTextBounds(text,0,text.length(), rect);
//    width = rect.width();
//     // 高度获取
//    Paint.FontMetricsInt fontMetricsInt = backPaint.getFontMetricsInt();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas != null) {

            Paint.FontMetricsInt fontMetricsInt = backPaint.getFontMetricsInt();

            // 注意：drawText x y 是一个 baseline 在 TextView 左下角的一个位置（但没到最左下边
            float baseX = getWidth() / 2 - backPaint.measureText(TEXT) / 2;
            float baseY = getHeight() / 2 - (fontMetricsInt.top + fontMetricsInt.bottom) / 2;
            canvas.drawText(TEXT, baseX, baseY, backPaint);
            canvas.save();

            Log.e(TAG, "x:" + baseX + " y:" + baseY + " w:" + getWidth() + " h:" + getHeight() + " top:" + fontMetricsInt.top + " bottom:" + fontMetricsInt.bottom + " measureWidth:" + backPaint.measureText(TEXT));

            Rect rect = new Rect(0, 0, (int) (bounds.width() * progress / 100), bounds.height());
            canvas.clipRect(rect);
            canvas.drawText(TEXT, baseX, baseY, forePaint);
            canvas.restore();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (progress == 100) {
                        progress = 0;
                    } else {
                        progress++;
                    }
                    postInvalidate();
                }
            }, 100);
        }
    }
}