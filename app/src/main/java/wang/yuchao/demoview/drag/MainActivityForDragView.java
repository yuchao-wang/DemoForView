package wang.yuchao.demoview.drag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import wang.yuchao.demoview.R;

/**
 * Author      : wangyuchao
 * DateTime    : 2018/7/4 16:26
 * Description :
 */
public class MainActivityForDragView extends Activity {

    public static Intent getIntentToMe(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivityForDragView.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_3);

        LinearLayout ll_container = (LinearLayout) findViewById(R.id.ll_container);

        for (int i = 0; i < 5; i++) {
            Button button = new Button(this);
            button.setText("拖动" + i);
            ll_container.addView(button);
        }

        for (int i = 0; i < ll_container.getChildCount(); i++) {
            View view = ll_container.getChildAt(i);
            DragUtils.setupDragSort(view, new DragUtils.DragListener() {
                @Override
                public void onDragStarted() {

                }

                @Override
                public void onDragEnded() {

                }
            });
        }
    }
}
