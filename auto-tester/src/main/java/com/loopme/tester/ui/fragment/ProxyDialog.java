package com.loopme.tester.ui.fragment;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.loopme.common.LoopMeProxy;
import com.loopme.tester.R;
import com.loopme.tester.ui.activity.BaseActivity;
import com.loopme.tester.utils.Utils;

import java.net.Proxy;
import java.util.ArrayList;

/**
 * Created by vynnykiakiv on 6/6/18.
 */

class ProxyDialog implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final BaseActivity mActivity;
    private final OnProxyChanged mListener;
    private final AlertDialog dialog;
    private final ArrayList<LoopMeProxy> mSavedProxyArray;

    public ProxyDialog(BaseActivity activity, OnProxyChanged listener) {
        mActivity = activity;
        mListener = listener;

        View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_proxy_layout, null);
        view.findViewById(R.id.dialog_proxy_ok_button).setOnClickListener(this);

        mSavedProxyArray = mActivity.getSavedProxy();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_spinner_item, Utils.asStringArray(mSavedProxyArray));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) view.findViewById(R.id.dialog_proxy_spinner);
        spinner.setAdapter(adapter);
        spinner.setSelection(Utils.getPosition(mSavedProxyArray, mActivity.getCurrentLoopMeProxy()));
        spinner.setOnItemSelectedListener(this);

        dialog = new AlertDialog.Builder(mActivity).setView(view).create();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_proxy_ok_button: {
                if (dialog != null) {
                    dialog.dismiss();
                    break;
                }
            }
        }
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String proxyName = adapterView.getItemAtPosition(position).toString();
        setNewProxy(proxyName);
    }

    private void setNewProxy(String proxyName) {
        for (LoopMeProxy proxy : mSavedProxyArray) {
            if (proxy.getName().equalsIgnoreCase(proxyName)) {
                saveCurrentChoice(proxy);
                onProxyChanged(proxy.toJavaProxy());
            }
        }
    }

    private void saveCurrentChoice(LoopMeProxy proxy) {
        if (mListener != null) {
            mListener.saveCurrentChoice(proxy);
        }
    }

    private void onProxyChanged(Proxy proxy) {
        if (mListener != null) {
            mListener.onProxyChanged(proxy);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface OnProxyChanged {
        void onProxyChanged(Proxy newProxy);

        void saveCurrentChoice(LoopMeProxy newProxy);
    }
}
