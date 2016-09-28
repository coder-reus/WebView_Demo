package android_h5_proj.zhiyi.com.android_h5_proj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.notification)
    TextView notification;
    @BindView(R.id.contacts)
    TextView contacts;
    @BindView(R.id.recorder)
    TextView recorder;
    @BindView(R.id.camera)
    TextView camera;
    @BindView(R.id.video)
    TextView video;
    @BindView(R.id.test_touch)
    TextView testTouch;
    @BindView(R.id.ediv)
    EditText ediv;
    @BindView(R.id.test)
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.location, R.id.notification, R.id.contacts, R.id.recorder, R.id.camera, R.id.video, R.id.test_touch, R.id.ediv, R.id.test})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.location:
                startActivity(new Intent(this, LocationActivity.class));
                break;
            case R.id.notification:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            case R.id.contacts:
                startActivity(new Intent(this, ContactsActivity.class));
                break;
            case R.id.recorder:
                startActivity(new Intent(this, AudioActivity.class));
                break;
            case R.id.camera:
                startActivity(new Intent(this, CameraImgsActivity.class));
                break;
            case R.id.video:
                startActivity(new Intent(this, VideosActivity.class));
                break;
            case R.id.test_touch:
                startActivity(new Intent(this, TestTouchEventDemo.class));
                break;
            case R.id.test:
                String url = ediv.getText().toString().trim();
                Intent i = new Intent(this, TestActivity.class);
                i.putExtra("url", url);
                startActivity(i);
                break;
        }
    }
}
