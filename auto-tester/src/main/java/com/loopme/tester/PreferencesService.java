package com.loopme.tester;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.loopme.common.LoopMeProxy;
import com.loopme.tester.ui.fragment.screen.InfoFragment;

import java.net.Proxy;
import java.util.ArrayList;

public class PreferencesService {
    public static final String IS_AUTO_LOADING_ENABLED = "is_auto_loading_enabled";
    private static final String GDPR_INTEGRATION_CASE = "GDPR_INTEGRATION_CASE";
    private static final String ARG_CURRENT_PROXY_NAME = "ARG_CURRENT_PROXY_NAME";
    private static final String ARG_CURRENT_PROXY_HOST = "ARG_CURRENT_PROXY_HOST";
    private static final String ARG_CURRENT_PROXY_PORT = "ARG_CURRENT_PROXY_PORT";

    private final Context mContext;
    private final SharedPreferences mPrefs;

    public PreferencesService(Context context) {
        mContext = context;
        mPrefs = getPrefs();
    }

    private SharedPreferences getPrefs() {
        return mContext.getSharedPreferences(PreferencesService.class.getName(), Application.MODE_PRIVATE);
    }

    public void setAutoLoadingState(final boolean autoloadingState) {
        mPrefs.edit().putBoolean(IS_AUTO_LOADING_ENABLED, autoloadingState).apply();
    }

    public boolean getAutoLoadingState() {
        return mPrefs.getBoolean(IS_AUTO_LOADING_ENABLED, false);
    }

    public void setGdprIntegrationCase(InfoFragment.GdprIntegrationCase gdprCase) {
        mPrefs.edit().putString(GDPR_INTEGRATION_CASE, gdprCase.name()).apply();
    }

    public InfoFragment.GdprIntegrationCase getGdprIntegrationCase() {
        return InfoFragment.GdprIntegrationCase.valueOf(mPrefs.getString(GDPR_INTEGRATION_CASE, InfoFragment.GdprIntegrationCase.IGNORE.name()));
    }

    public ArrayList<LoopMeProxy> getSavedProxyArray() {
        ArrayList<LoopMeProxy> proxies = new ArrayList<>();
        proxies.add(new LoopMeProxy("Jacob", "10.0.0.121", 8888));
        proxies.add(new LoopMeProxy("Kate", "10.0.0.65", 8888));
        proxies.add(LoopMeProxy.NO_PROXY);
        return proxies;
    }

    public void saveAsCurrentLoopMeProxy(LoopMeProxy loopMeProxy) {
        mPrefs.edit().putString(ARG_CURRENT_PROXY_NAME, loopMeProxy.getName()).apply();
        mPrefs.edit().putString(ARG_CURRENT_PROXY_HOST, loopMeProxy.getHost()).apply();
        mPrefs.edit().putInt(ARG_CURRENT_PROXY_PORT, loopMeProxy.getPort()).apply();
    }

    public LoopMeProxy getCurrentLoopMeProxy() {
        String name = mPrefs.getString(ARG_CURRENT_PROXY_NAME, "NONE");
        String host = mPrefs.getString(ARG_CURRENT_PROXY_HOST, "");
        int port = mPrefs.getInt(ARG_CURRENT_PROXY_PORT, 0);
        return new LoopMeProxy(name, host, port);
    }
}