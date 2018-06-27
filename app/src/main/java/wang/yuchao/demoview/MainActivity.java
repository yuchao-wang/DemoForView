package wang.yuchao.demoview;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class MainActivity extends Activity {

    public static float dpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initImageView();
    }

    private void initImageView() {
        // 初始化默认大小是 100 * 100
        final float minWidth = dpToPixel(100);
        final float minHeight = dpToPixel(100);

        final LinearLayout llContainer = (LinearLayout) findViewById(R.id.llContainer);
        final LinearLayout llParent = (LinearLayout) findViewById(R.id.llParent);
        final SeekBar seekBarWidth = (SeekBar) findViewById(R.id.seekImageWidth);
        final SeekBar seekBarHeight = (SeekBar) findViewById(R.id.seekImageHeight);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llParent.getLayoutParams();

                layoutParams.width = (int) (minWidth + (llContainer.getWidth() - minWidth) * seekBarWidth.getProgress() / 100);
                layoutParams.height = (int) (minHeight + (llContainer.getHeight() - minHeight) * seekBarHeight.getProgress() / 100);

                llParent.setLayoutParams(layoutParams);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };

        seekBarWidth.setOnSeekBarChangeListener(listener);
        seekBarHeight.setOnSeekBarChangeListener(listener);
    }


}
