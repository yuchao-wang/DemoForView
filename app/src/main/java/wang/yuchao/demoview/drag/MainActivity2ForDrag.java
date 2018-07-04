package wang.yuchao.demoview.drag;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;

import wang.yuchao.demoview.R;

/**
 * Author      : wangyuchao
 * DateTime    : 2018/7/4 15:03
 * Description :
 */
public class MainActivity2ForDrag extends Activity {
    private static final String TAG = "wang";
    private TextView iv1;
    private TextView iv2;

    public static Intent getIntentToMe(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivity2ForDrag.class);
        return intent;
    }

    // LinearLayout 的拖放操作和动画
    // https://blog.zhanghai.me/drag-and-drop-with-animation-on-linearlayout/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        iv1 = (TextView) findViewById(R.id.iv1);
        iv2 = (TextView) findViewById(R.id.iv2);

        iv1.setOnLongClickListener(new View.OnLongClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean onLongClick(View v) {

                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);

                ClipData clipData = ClipData.newPlainText("dot", "DOT:" + v.toString());

                v.startDragAndDrop(clipData, dragShadowBuilder, v, 0);

                return true;
            }
        });

        // 参考：https://developer.android.com/guide/topics/ui/drag-drop?hl=zh-cn#AboutDragEvent
        iv1.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {

                int action = event.getAction();
                switch (action) {
                    // 开始拖拽
                    case DragEvent.ACTION_DRAG_STARTED:
                        iv1.setText("开始");
                        break;
                    // 结束拖拽
                    case DragEvent.ACTION_DRAG_ENDED:
                        iv1.setText("结束");
                        break;
                    // 拖拽进某个控件后，退出
                    case DragEvent.ACTION_DRAG_EXITED:
                        iv1.setText("进入又退出");
                        break;
                    // 拖拽进某个控件后，保持
                    case DragEvent.ACTION_DRAG_ENTERED:
                        iv1.setText("进入保持");
                        break;
                    // 推拽进入某个控件
                    case DragEvent.ACTION_DRAG_LOCATION:
                        iv1.setText("拖入了");
                        break;
                    // 推拽进入某个控件，后在该控件内，释放。即把推拽控件放入另一个控件
                    case DragEvent.ACTION_DROP:
                        iv1.setText("放入了");
                        break;
                }

                return true;
            }
        });

        // 参考：https://developer.android.com/guide/topics/ui/drag-drop?hl=zh-cn#AboutDragEvent
        iv2.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();
                switch (action) {
                    // 开始拖拽
                    case DragEvent.ACTION_DRAG_STARTED:
                        Log.d(TAG, "开始");
                        iv2.setText("开始");
                        break;
                    // 拖拽进某个控件后，保持
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(TAG, "进入保持");
                        iv2.setText("进入保持");
                        break;
                    // 推拽进入某个控件
                    case DragEvent.ACTION_DRAG_LOCATION:
                        Log.d(TAG, "拖入了");
                        iv2.setText("拖入了");
                        break;
                    // 拖拽进某个控件后，退出
                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d(TAG, "进入又退出");
                        iv2.setText("进入又退出");
                        break;
                    // 推拽进入某个控件，后在该控件内，释放。即把推拽控件放入另一个控件
                    case DragEvent.ACTION_DROP:
                        Log.d(TAG, "释放");
                        iv2.setText("释放");
                        break;
                    // 结束拖拽
                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.d(TAG, "结束");
                        iv2.setText("结束");
                        break;
                }
                return true;
            }
        });
    }
}
