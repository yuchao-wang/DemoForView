package wang.yuchao.demoview.drag;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;

import wang.yuchao.demoview.R;

/**
 * Author      : wangyuchao
 * DateTime    : 2018/7/4 16:26
 * Description :
 */
public class MainActivityForDragView extends Activity {

    private static final String TAG = "wang";

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
            setViewDragListener(view);
        }
    }

    private void setViewDragListener(View view) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.startDrag(null, new View.DragShadowBuilder(view), new DragState(view), 0);
                return true;
            }
        });

        view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent event) {

                ViewGroup viewGroup = (ViewGroup) view.getParent();

                DragState dragState = (DragState) event.getLocalState();
                View dragView = dragState.view;

                int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED: {
                        if (view == dragState.view) {
                            view.setVisibility(View.INVISIBLE);
                        }
                    }
                    break;
                    case DragEvent.ACTION_DRAG_LOCATION: {
                        if (view != dragView) {
                            swapChild(viewGroup, dragView, view);
                        }
                    }
                    break;
                    case DragEvent.ACTION_DROP:
                    case DragEvent.ACTION_DRAG_ENDED: {
                        if (view == dragState.view) {
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
                }
                return true;
            }
        });
    }

    // 交换两个子 View
    private void swapChild(ViewGroup viewGroup, View dragView, View targetView) {
        int targetIndex = viewGroup.indexOfChild(targetView);
        int dragIndex = viewGroup.indexOfChild(dragView);

        float viewY = targetView.getY();

        if (dragIndex < targetIndex) {
            viewGroup.removeViewAt(targetIndex);    // 移除 index 大的
            viewGroup.removeViewAt(dragIndex);      // 移除 index 小的
            viewGroup.addView(targetView, dragIndex);   // 添加 index 小的
            viewGroup.addView(dragView, targetIndex);   // 添加 index 大的
        } else {
            viewGroup.removeViewAt(dragIndex);      // 移除 index 小的
            viewGroup.removeViewAt(targetIndex);    // 移除 index 大的
            viewGroup.addView(dragView, targetIndex);   // 添加 index 大的
            viewGroup.addView(targetView, dragIndex);   // 添加 index 小的
        }

        // 添加动画
        runAnimation(targetView, viewY);
    }

    private void runAnimation(final View view, final float viewY) {
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (observer.isAlive()) {
                    observer.removeOnPreDrawListener(this);
                }
                ObjectAnimator
                        .ofFloat(view, View.Y, viewY, view.getTop())
                        .setDuration(500)
                        .start();
                return true;
            }
        });
    }

    // 拖动的状态
    private static class DragState {
        public int index;
        public View view;

        public DragState(View view) {
            this.view = view;
            this.index = ((ViewGroup) view.getParent()).indexOfChild(view);
        }
    }
}
