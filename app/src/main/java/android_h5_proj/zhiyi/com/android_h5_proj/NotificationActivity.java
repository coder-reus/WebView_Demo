package android_h5_proj.zhiyi.com.android_h5_proj;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/7.
 */
public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.location_web)
    WebView locationWebView;
    private WebSettings locationWebSettings;
    private boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);


        locationWebSettings = locationWebView.getSettings();
        locationWebSettings.setJavaScriptEnabled(true);
        locationWebView.setHapticFeedbackEnabled(false);
        locationWebView.addJavascriptInterface(new JavaScriptInterface(this), "contacts");
        locationWebView.setWebChromeClient(new WebChromeClient() {
        });
        locationWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                isLoaded = true;
            }
        });


        try {
            StringBuffer sb = new StringBuffer();
            InputStream is = this.getAssets().open("notification.html");
            int len;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, "utf-8"));
            }
            is.close();
            locationWebView.loadDataWithBaseURL(null, sb.toString(), "text/html", "UTF-8", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class JavaScriptInterface {
        Context mContext;

        JavaScriptInterface(Context c) {
            this.mContext = c;
        }

        @JavascriptInterface
        public void onClickToContacts() {

        }
    }
}
