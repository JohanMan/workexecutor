package com.johan.workexecutor;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * Created by Administrator on 2016/9/26.
 */
public class WorkThreadHandler {

    private HandlerThread handlerThread;
    private Handler handler;

    public WorkThreadHandler() {
        handlerThread = new HandlerThread("handler_thread_" + System.currentTimeMillis());
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    public Handler getHandler() {
        return handler;
    }

    public void quitHandler() {
        if (!handlerThread.isInterrupted()) {
            handlerThread.quit();
            handlerThread.interrupt();
        }
    }

}
