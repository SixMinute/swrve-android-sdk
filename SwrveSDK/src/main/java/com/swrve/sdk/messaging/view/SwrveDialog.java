package com.swrve.sdk.messaging.view;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.swrve.sdk.R;
import com.swrve.sdk.SwrveLogger;
import com.swrve.sdk.messaging.SwrveMessage;

/**
 * Dialog used to display in-app messages.
 */
public class SwrveDialog extends Dialog {
    private SwrveMessageView innerView;
    private SwrveMessage message;
    private final boolean hideToolbar;
    private LayoutParams originalParams;

    private boolean dismissed = false;

    public SwrveDialog(Activity context, SwrveMessage message, SwrveMessageView innerView, boolean hideToolbar) {
        super(context, R.style.SwrveDialogTheme);
        this.message = message;
        this.innerView = innerView;
        this.hideToolbar = hideToolbar;
        this.originalParams = context.getWindow().getAttributes();
        setContentView(innerView);
        setOwnerActivity(context);
        innerView.setContainerDialog(this);
    }

    public SwrveMessage getMessage() {
        return message;
    }

    public SwrveMessageView getInnerView() {
        return innerView;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (hideToolbar) {
            // Remove the status bar from the activity
            WindowManager.LayoutParams attrs = getWindow().getAttributes();
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(attrs);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        goneAway();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        // Notify the campaign one of its messages was dismissed
        message.getCampaign().messageDismissed();
        goneAway();
    }

    private void goneAway() {
        if (!dismissed) {
            dismissed = true;
            if (hideToolbar) {
                try {
                    // Restore the window attributes
                    getWindow().setAttributes(originalParams);
                } catch (IllegalArgumentException exp) {
                    // Dialog was not on assigned to a parent view
                    SwrveLogger.e(Log.getStackTraceString(exp));
                }
            }
        }
    }
}
