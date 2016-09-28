package android_h5_proj.zhiyi.com.android_h5_proj;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/7.
 */
public class TipsAlertDailog extends AlertDialog {
    protected TipsAlertDailog(Context context) {
        super(context);
    }

    protected TipsAlertDailog(Context context, int theme) {
        super(context, theme);
    }

    protected TipsAlertDailog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private Button cancelBtn, goonBtn;
    private TextView tipsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alertdialog_tips);
        cancelBtn = (Button) findViewById(R.id.cancel_action);
        goonBtn = (Button) findViewById(R.id.goon_action);
        tipsTV = (TextView) findViewById(R.id.content_tips);
    }

    public void setTipsAndListener(String tips, View.OnClickListener cancelListener, View.OnClickListener goonListener) {
        tipsTV.setText(tips);
        if (cancelListener != null) {
            cancelBtn.setOnClickListener(cancelListener);
        }
        if (goonListener != null) {
            goonBtn.setOnClickListener(goonListener);
        }
    }
}
