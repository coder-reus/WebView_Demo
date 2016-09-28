package android_h5_proj.zhiyi.com.android_h5_proj;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/7/13.
 */
public class VideosActivity extends BaseActivity {
    private static final String LOGTAG = "CameraImgsActivity";
    private WebView locationWebView;
    private WebSettings locationWebSettings;
    private boolean isLoaded = false;
    private int index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);


        locationWebView = (WebView) findViewById(R.id.location_web);
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

        locationWebView.loadUrl("file:///android_asset/page_video.html");
    }

    public class JavaScriptInterface {
        Context mContext;

        JavaScriptInterface(Context c) {
            this.mContext = c;
        }

        @JavascriptInterface
        public void oncamera() {
            takePhoto();
        }

        @JavascriptInterface
        public void onphotos() {
            try {
                Intent photo = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photo, PHOTO_PICKED_WITH_DATA);
            } catch (ActivityNotFoundException e) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
                startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
            }
        }
    }

    private void takePhoto() {
        try{
            Intent camera = new Intent(MediaStore.ACTION_VIDEO_CAPTURE); //调用系统相机
            String dirPath = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/";
            File dirFile = new File(dirPath);
            if (!dirFile.exists()) dirFile.mkdirs();
            File file = new File(dirFile, "video_0" + (index++) + ".mp4");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cameraUri = Uri.fromFile(file);
            camera.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            startActivityForResult(camera, CAMERA_WITH_DATA);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this,"没拍照权限",Toast.LENGTH_SHORT).show();
        }
    }

    private static Uri cameraUri;


    //用户图片操作部分
    private static final int CAMERA_WITH_DATA = 10;   //拍照
    private static final int PHOTO_PICKED_WITH_DATA = 11;  //gallery

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PHOTO_PICKED_WITH_DATA://相册
                if (data != null) {
                    //跳转到剪裁页面
                    try {
                        Uri selectedImage = data.getData();
                        String[] filePathColumns = {MediaStore.Video.Media.DATA};
                        Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                        if (c != null) {
                            if (c.moveToNext()) {
                                c.moveToFirst();
                                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                                String picturePath = "file://" + c.getString(columnIndex);
                                setCropImg(picturePath);
                            }
                            c.close();
                        } else {
                            String picturePath = selectedImage.toString();
                            setCropImg(picturePath);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CAMERA_WITH_DATA: // 照相机程序返回的,再次调用图片剪辑程序去修剪图片
                if (cameraUri == null) {
                    return;
                }
                String photoUrl = "file://" + cameraUri.getPath();
                setCropImg(photoUrl);
                break;
        }
    }


    /**
     * 裁剪图片为圆形,并完成图片的上传:改为二次采样的方式来截取图片
     */
    public void setCropImg(final String tempDir) {
        Log.e(LOGTAG, tempDir);
        if (tempDir != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    locationWebView.loadUrl("javascript:add_video_item('" + tempDir + "')");
                }
            });
        }
    }
}
