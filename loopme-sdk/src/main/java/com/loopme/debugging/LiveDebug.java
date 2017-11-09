package com.loopme.debugging;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;

import com.loopme.BuildConfig;
import com.loopme.Constants;
import com.loopme.HttpUtil;
import com.loopme.Logging;
import com.loopme.request.RequestUtils;
import com.loopme.utils.Utils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LiveDebug {

    private static final String LOG_TAG = LiveDebug.class.getSimpleName();

    //default debug time is 5 minutes
    private static final int DEBUG_TIME = 5 * 60 * 1000;

    private static LogDbHelper sLogDbHelper;
    private static ExecutorService sExecutor = Executors.newSingleThreadExecutor();

    private static CountDownTimer sDebugTimer;
    private static boolean sIsDebugOn;

    public static void init(Context context) {
        sLogDbHelper = new LogDbHelper(context);
    }

    public static void setLiveDebug(final Context context, final boolean debug, final String appKey) {
        Logging.out(LOG_TAG, "setLiveDebug " + debug);
        if (sIsDebugOn != debug) {
            if (debug) {
                sIsDebugOn = debug;
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        startTimer(context, appKey);
                    }
                });
            }
        }
    }

    public static void handle(String logTag, String text) {
        if (sIsDebugOn) {
            saveLog(logTag, text);
        }
    }

    private static void startTimer(final Context context, final String appKey) {
        if (sDebugTimer == null) {
            sDebugTimer = new CountDownTimer(DEBUG_TIME, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    sendToServer(context, appKey);
                    sIsDebugOn = false;
                    sDebugTimer = null;
                }
            };
            Logging.out(LOG_TAG, "start debug timer");
            sDebugTimer.start();
        }
    }

    private static void sendToServer(final Context context, final String appKey) {
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                if (sLogDbHelper != null) {
                    Logging.out(LOG_TAG, "send to server");
                    Map<String, String> params = initPostDataParams(context, appKey);
                    HttpUtil.sendRequest(Constants.ERROR_URL, null, HttpUtil.obtainRequestString(params));
                }
            }
        });
    }

    private static Map<String, String> initPostDataParams(Context context, String appKey) {
        String debugLogs = initLogsString();

        Map<String, String> params = new HashMap<>();
        params.put(Params.DEVICE_OS, Constants.ADNROID_DEVICE_OS);
        params.put(Params.SDK_TYPE, Constants.LOOPME_SDK_TYPE);
        params.put(Params.SDK_VERSION, BuildConfig.VERSION_NAME);
        params.put(Params.DEVICE_ID, RequestUtils.getAdvertisingIdInfo(context));
        params.put(Params.PACKAGE_ID, context.getPackageName());
        params.put(Params.APP_KEY, appKey);
        params.put(Params.MSG, Constants.SDK_DEBUG_MSG);
        params.put(Params.DEBUG_LOGS, debugLogs);
        params.put(Params.APP_IDS, Utils.getPackageInstalledEncrypted());

        return params;
    }

    private static String initLogsString() {
        if (sLogDbHelper != null) {
            List<String> logList = sLogDbHelper.getLogs();
            sLogDbHelper.clear();
            StringBuilder sb = new StringBuilder();
            for (String s : logList) {
                sb.append(s);
                sb.append("\n");
            }
            return sb.toString();
        }
        return null;
    }

    private static void saveLog(String logTag, String text) {
        final String logString = formatLogMessage(logTag, text);
        if (sLogDbHelper != null) {
            sExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    sLogDbHelper.putLog(logString);
                }
            });
        }
    }

    private static String formatLogMessage(String logTag, String text) {
        String thread = (Looper.getMainLooper() == Looper.myLooper()) ? "ui" : "bg";
        return MessageFormat.format("{0}: {1}: {2}", thread, logTag, text);
    }

    public static boolean isDebugOn() {
        return sIsDebugOn;
    }
}