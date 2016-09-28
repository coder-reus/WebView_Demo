package android_h5_proj.zhiyi.com.android_h5_proj;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/8.
 */
public class AudioActivity extends AppCompatActivity {
    private static final String LOG_TAG = "audio_activity";
    @BindView(R.id.location_web)
    WebView locationWebView;

    private WebSettings locationWebSettings;
    private String fileName, filePath;
    //设置sdcard的路径
    private String fileDirs = Environment.getExternalStorageDirectory().getAbsolutePath();
    private int index = 0;
    private boolean isLoaded = false;

    //语音操作对象
    private MediaRecorder mRecorder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        ButterKnife.bind(this);

        locationWebSettings = locationWebView.getSettings();
        locationWebSettings.setJavaScriptEnabled(true);
        locationWebView.setHapticFeedbackEnabled(false);
        locationWebView.addJavascriptInterface(new JavaScriptInterface(this), "android");
        locationWebView.setWebChromeClient(new WebChromeClient() {
        });
        locationWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isLoaded = true;
            }
        });

        locationWebView.loadUrl("file:///android_asset/page_audio.html");


    }

    public class JavaScriptInterface {
        Context mContext;

        JavaScriptInterface(Context c) {
            this.mContext = c;
        }

        @JavascriptInterface
        public void onstartrecord() {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            fileName = "/audio_test" + (index++) + ".3gp";
            filePath = fileDirs + fileName;
            mRecorder.setOutputFile(filePath);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e(LOG_TAG, "prepare() failed");
            }
            mRecorder.start();
        }


        @JavascriptInterface
        public void onfinishedrecord() {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    locationWebView.loadUrl("javascript:add_audio_item(" + "'" + "file://" + filePath + "'" + ")");
                }
            });
        }

        /*@JavascriptInterface
        public void onStartPlay() {
            mPlayer = new MediaPlayer();
            try {
                mPlayer.setDataSource(fileName);
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                Log.e(LOG_TAG, "播放失败");
            }
        }

        @JavascriptInterface
        public void onEndPlay() {
            mPlayer.release();
            mPlayer = null;
        }*/
    }
}
