package android_h5_proj.zhiyi.com.android_h5_proj;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/6.
 */
public class LocationActivity extends AppCompatActivity implements LocationUtils.LocatedCallback {


    @BindView(R.id.location_web)
    WebView locationWebView;
    @BindView(R.id.locating_progress_bar)
    LinearLayout locatingProgress;
    private WebSettings locationWebSettings;
    private boolean isLoaded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);

        locationWebView = (WebView) findViewById(R.id.location_web);
        locatingProgress = (LinearLayout) findViewById(R.id.locating_progress_bar);
        locatingProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("test", "locating_progress_showing");
            }
        });

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


        locationWebView.loadUrl("file:///android_asset/location.html");
    }

    private LocationUtils locationUtils;

    @Override
    protected void onResume() {
        super.onResume();
        locationUtils = new LocationUtils();
        locationUtils.initLoacation(LocationActivity.this);
        locationUtils.setLocatedCallback(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationUtils.stopLoacation();
    }

    private String tips;

    @Override
    public void locatedCallback(final String locatedInfo) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                locatingProgress.setVisibility(View.GONE);
                if (TextUtils.isEmpty(locatedInfo)) {
                    locationWebView.loadUrl("javascript:loaded_info(" + "'定位失败'" + ")");
                } else {
                    locationWebView.loadUrl("javascript:loaded_info(" + "'" + locatedInfo + "'" + ")");
                }
            }
        });
    }

    public class JavaScriptInterface {
        Context mContext;

        JavaScriptInterface(Context c) {
            this.mContext = c;
        }

        @JavascriptInterface
        public void onClickToLocation() {
            //android.permission.ACCESS_FINE_LOCATION（精确定位）
            //android.permission.ACCESS_COARSE_LOCATION（粗糙定位）

            if (isLoaded) {
                PackageManager pm = getPackageManager();
                try {
                    PackageInfo packageInfo = pm.getPackageInfo(LocationActivity.this.getPackageName(), 0);
                    boolean isFineLocation = (PackageManager.PERMISSION_GRANTED ==
                            pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", packageInfo.packageName));

                    if (isFineLocation) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                locatingProgress.setVisibility(View.VISIBLE);
                            }
                        });
                        locationUtils.getProvinceAndCity();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                locationWebView.loadUrl("javascript:loaded_info(" + "'没有定位权限，定位失败'" + ")");
                            }
                        });
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
