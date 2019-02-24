package com.suji.ish.suji.view;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.suji.ish.suji.R;
import com.suji.ish.suji.rxjava.InternetEvent;

import razerdp.basepopup.BasePopupWindow;

/**
 * @author ish
 */
public class NetWorkAlertDialog extends BasePopupWindow {
    View mView;

    public NetWorkAlertDialog(Context context) {
        super(context);
    }

    // 必须实现，这里返回您的contentView
    // 为了让库更加准确的做出适配，强烈建议使用createPopupById()进行inflate
    @Override
    public View onCreateContentView() {
        mView = createPopupById(R.layout.dialog_network_alert);
        return mView;
    }

    public void setView(InternetEvent event) {
        ImageView imageView = getContentView().findViewById(R.id.alert_result_icon);
        TextView tx = getContentView().findViewById(R.id.alert_result_text);

        if (event.getCode() == InternetEvent.FAIL_NO_NETWORK) {
            imageView.setImageResource(R.drawable.network);
            tx.setText(event.getMessage());
        } else if (event.getCode() == InternetEvent.FAIL_NO_RESOURCE) {
            imageView.setImageResource(R.drawable.error);
            tx.setText(event.getMessage());
        }
    }

    // 以下为可选代码（非必须实现）
    // 返回作用于PopupWindow的show和dismiss动画，本库提供了默认的几款动画，这里可以自由实现
    @Override
    protected Animation onCreateShowAnimation() {
        return getDefaultScaleAnimation(true);
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return getDefaultScaleAnimation(false);
    }
}