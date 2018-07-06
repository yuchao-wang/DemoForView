package wang.yuchao.demoview.drag;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    // 参考 ：https://blog.zhanghai.me/drag-and-drop-with-animation-on-linearlayout/
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
                        // 注意：快速拖动时由于来不及向每个经过的视图发送消息，因此会造成顺序紊乱的问题
                        // 解决：递归交换视图
                        if (view != dragView) {
                            // 递归调用或者循环解决拖动问题
                            swapViews(viewGroup, dragState, view);
                            // swapViews2(viewGroup, dragState, view);

                            // 注意：直接交换会出现快速拖动 BUG
                            // swapChild(viewGroup, dragState, view);
                        }
                    }
                    break;

                    case DragEvent.ACTION_DROP: {
                        Button button = (Button) view;
                        Log.e(TAG, ">>>>>>>>>>> DROP: view 针对 DragView " + button.getText());
                        showAllView(viewGroup);
                    }
                    break;
                    case DragEvent.ACTION_DRAG_ENDED: {
                        Button button = (Button) view;
                        Log.e(TAG, ">>>>>>>>>>> END: view 针对 TargetView " + button.getText());
                        if (view == dragState.view) {
                            // 注意：不知道为啥 showAllView 必须要调用两次
                            showAllView(viewGroup);
                        }
                    }
                    break;
                }
                return true;
            }
        });
    }

    // 循环交换
    private void swapViews2(ViewGroup viewGroup, DragState dragState, View targetView) {
        int targetIndex = viewGroup.indexOfChild(targetView);
        int dragIndex = dragState.index;

        if (targetIndex > dragIndex) {
            for (int i = dragIndex; i < targetIndex; i++) {
                swapChild(viewGroup, dragState, viewGroup.getChildAt(i + 1));
            }
        } else {
            for (int i = dragIndex; i > targetIndex; i--) {
                swapChild(viewGroup, dragState, viewGroup.getChildAt(i - 1));
            }
        }
    }

    private void showAllView(ViewGroup viewGroup) {
        // 所有恢复显示
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View view = viewGroup.getChildAt(i);
            boolean visible = (view.getVisibility() == View.VISIBLE);
            Log.e(TAG, "index:" + i + " >>> visible:" + visible);
            if (!visible) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    // 递归交换
    private void swapViews(ViewGroup viewGroup, DragState dragState, View targetView) {
        // 递归判断是否要交换
        swapChildren(viewGroup, dragState, targetView);
        // 交换操作
        swapChild(viewGroup, dragState, targetView);
    }

    // 先交换其他的
    private void swapChildren(ViewGroup viewGroup, DragState dragState, View targetView) {
        int targetIndex = viewGroup.indexOfChild(targetView);
        int dragIndex = dragState.index;

        // 判断两边距离是否大于1
        if ((targetIndex - dragIndex) > 1) {
            swapViews(viewGroup, dragState, viewGroup.getChildAt(targetIndex - 1));
        } else if ((dragIndex - targetIndex) > 1) {
            swapViews(viewGroup, dragState, viewGroup.getChildAt(targetIndex + 1));
        }
    }

    // 交换两个子 View
    private void swapChild(ViewGroup viewGroup, DragState dragState, View targetView) {
        View dragView = dragState.view;
        int dragIndex = viewGroup.indexOfChild(dragView);
        int targetIndex = viewGroup.indexOfChild(targetView);

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

        // 交换 index
        dragState.index = targetIndex;

        // 添加动画
        runAnimation(targetView, viewY);
    }

    // 动画预览
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
                        .setDuration(250)
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
