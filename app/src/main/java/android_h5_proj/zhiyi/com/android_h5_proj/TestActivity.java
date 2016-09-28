package android_h5_proj.zhiyi.com.android_h5_proj;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/7/8.
 */
public class TestActivity extends AppCompatActivity {
    private WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        url = getIntent().getStringExtra("url");

        if (TextUtils.isEmpty(url)) {
            url = "http://m.toutiao.com/i6333172728081678849/?tt_from=weixin&utm_campaign=client_share&from=singlemessage&app=news_article&utm_source=weixin&isappinstalled=1&iid=5485409922&utm_medium=toutiao_android&wxshare_count=1";
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        webView = (WebView) findViewById(R.id.test_web);
        //启动缓存
        webView.getSettings().setAppCacheEnabled(true);
        //设置缓存模式
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        //提高渲染的优先级
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        //允许JS执行
        webView.getSettings().setJavaScriptEnabled(true);
        //把图片加载放在最后来加载渲染
        webView.getSettings().setBlockNetworkImage(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.requestFocus(View.FOCUS_DOWN);


        Log.e("**********", "****url****" + url);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!TextUtils.isEmpty(url) && !url.startsWith("http") && !url.startsWith("https") && !url.startsWith("ftp")) {
                    Uri uri = Uri.parse(url);
                    Intent i = new Intent();
                    i.setData(uri);
                    startActivity(i);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (!TextUtils.isEmpty(url) && url.startsWith("sns")) {
                    Toast.makeText(TestActivity.this, "系统暂不支持该功能", Toast.LENGTH_SHORT).show();
                    view.stopLoading();
                }
                //Auto - generated method stub
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (!TextUtils.isEmpty(url) && url.startsWith("sns")) {
                    Toast.makeText(TestActivity.this, "系统暂不支持该功能", Toast.LENGTH_SHORT).show();
                    view.stopLoading();
                }
                //Auto - generated method stub
                super.onPageStarted(view, url, favicon);
            }
        });
    }
}

