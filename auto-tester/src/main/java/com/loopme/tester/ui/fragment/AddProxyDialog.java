package com.loopme.tester.ui.fragment;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.loopme.common.LoopMeProxy;
import com.loopme.tester.R;
import com.loopme.tester.ui.activity.BaseActivity;

/**
 * Created by vynnykiakiv on 6/6/18.
 */

class AddProxyDialog implements View.OnClickListener {
    private final BaseActivity mActivity;
    private final AlertDialog dialog;
    private final TextView mNameTextView;
    private final TextView mHostTextView;
    private final TextView mPortTextView;

    public AddProxyDialog(BaseActivity activity) {
        mActivity = activity;
        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_add_proxy_layout, null);
        view.findViewById(R.id.dialog_add_proxy_save_button).setOnClickListener(this);
        view.findViewById(R.id.dialog_add_proxy_cancel_button).setOnClickListener(this);

        mNameTextView = (TextView) view.findViewById(R.id.dialog_add_proxy_name_edit_text);
        mHostTextView = (TextView) view.findViewById(R.id.dialog_add_proxy_host_edit_text);
        mPortTextView = (TextView) view.findViewById(R.id.dialog_add_proxy_port_edit_text);

        dialog = new AlertDialog.Builder(mActivity).setView(view).create();
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_add_proxy_save_button: {
                saveNewProxy();
                break;
            }
        }
        dismiss();
    }

    private void saveNewProxy() {
        String name = mNameTextView.getText().toString();
        String host = mHostTextView.getText().toString();
        int port = Integer.parseInt(mPortTextView.getText().toString());
        if (mActivity != null) {
            mActivity.addNewPoxy(new LoopMeProxy(name, host, port));
        }
    }

    private void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
