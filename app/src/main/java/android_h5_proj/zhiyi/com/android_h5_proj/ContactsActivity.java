package android_h5_proj.zhiyi.com.android_h5_proj;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/7/7.
 */
public class ContactsActivity extends AppCompatActivity {

    @BindView(R.id.location_web)
    WebView locationWebView;
    private WebSettings locationWebSettings;
    private boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
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


        locationWebView.loadUrl("file:///android_asset/contacts.html");

    }


    /**
     * 读取通讯录
     */
    String contacts = "";

    private void readContacts() {
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        int contactIdIndex = 0;
        String name = "";

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            }

            while (cursor.moveToNext()) {
                String contactId = cursor.getString(contactIdIndex);
                //查找该联系人的phone信息
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                        null, null);
                int phoneIndex = 0;
                if (phones != null) {
                    if (phones.getCount() > 0) {
                        phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    }
                    while (phones.moveToNext()) {
                        String phoneNumber = phones.getString(phoneIndex);
                        if (!TextUtils.isEmpty(phoneNumber)) {
                            name = getContactName(phoneNumber);
                            if (TextUtils.isEmpty(contacts)) {
                                contacts = name + ":" + phoneNumber;
                            } else {
                                contacts = contacts + "," + name + ":" + phoneNumber;
                            }
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, "暂时未发现可添加的通讯录联系人", Toast.LENGTH_SHORT).show();
        }
    }

    public String getContactName(String mNumber) {
        String name = "";
        String[] projection = {ContactsContract.PhoneLookup.DISPLAY_NAME};

        Cursor cursor = this.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                ContactsContract.CommonDataKinds.Phone.NUMBER + " = '" + mNumber + "'",
                null,
                null);
        if (cursor == null) {
            return "";
        }
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);

            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            name = cursor.getString(nameFieldColumnIndex);
            break;
        }
        cursor.close();
        return name;
    }


    public class JavaScriptInterface {
        Context mContext;

        JavaScriptInterface(Context c) {
            this.mContext = c;
        }

        @JavascriptInterface
        public void onClickToContacts() {
            if (isLoaded) {
                PackageManager pm = getPackageManager();
                try {
                    PackageInfo packageInfo = pm.getPackageInfo(ContactsActivity.this.getPackageName(), 0);
                    final boolean isFineLocation = (PackageManager.PERMISSION_GRANTED ==
                            pm.checkPermission("android.permission.READ_CONTACTS", packageInfo.packageName));
                    String tips;
                    if (isFineLocation) {
                        tips = "有读取通讯录的这个权限";
                    } else {
                        tips = "没有读取通讯录的这个权限";
                    }
                    readContacts();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isFineLocation) {
                                locationWebView.loadUrl("javascript:display_contacts(" + "'" + contacts + "'" + ")");
                            } else {
                                final TipsAlertDailog tipsAlertDailog = new TipsAlertDailog(ContactsActivity.this);
                                tipsAlertDailog.show();
                                tipsAlertDailog.setTipsAndListener("没有读取通讯录的这个权限，请在 系统设置 中的 权限管理 中，将读取通讯录改为允许", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tipsAlertDailog.dismiss();
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        tipsAlertDailog.dismiss();
                                    }
                                });
                            }
                        }
                    });
                    Toast.makeText(ContactsActivity.this, tips, Toast.LENGTH_SHORT).show();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(ContactsActivity.this, "网页脚本为加载完毕。", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
