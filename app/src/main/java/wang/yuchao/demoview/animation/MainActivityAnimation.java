package wang.yuchao.demoview.animation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import wang.yuchao.demoview.R;

/**
 * Author      : wangyuchao
 * DateTime    : 2018/7/5 17:19
 * Description :
 */
public class MainActivityAnimation extends Activity {

    public static Intent getIntentToMe(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, MainActivityAnimation.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_animation);
    }
}
