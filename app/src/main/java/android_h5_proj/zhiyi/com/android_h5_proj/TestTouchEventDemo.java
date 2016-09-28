package android_h5_proj.zhiyi.com.android_h5_proj;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jacen on 2016/9/20.
 */
public class TestTouchEventDemo extends AppCompatActivity {
    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.tv)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch_event);
        ButterKnife.bind(this);
        tv.setText("可以吗？");
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float rawX = event.getRawX();
                float y = event.getX();
                float rawY = event.getRawX();
                Log.e("******", "x-->" + x);
                Log.e("******", "y-->" + y);
                Log.e("******", "rawX-->" + rawX);
                Log.e("******", "rawY-->" + rawY);
                return false;
            }
        });
    }
}
