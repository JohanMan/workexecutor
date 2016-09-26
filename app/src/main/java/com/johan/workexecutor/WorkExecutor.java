package com.johan.workexecutor;

import android.os.Handler;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

/**
 * Created by Administrator on 2016/9/26.
 */
public class WorkExecutor implements WorkResultReceiver {

    private WorkThreadHandler workThreadHandler;

    private WeakReference<Handler> uiHandler;
    private Handler backgroundHandler;

    private LinkedList<Worker<?, ?>> workerList = new LinkedList<>();

    private ExceptionWorker exceptionWorker;

    private DataProvider originalDataProvider;

    public WorkExecutor() {
        uiHandler = new WeakReference<>(new Handler());
        workThreadHandler = new WorkThreadHandler();
        backgroundHandler = workThreadHandler.getHandler();
    }

    public WorkExecutor initDataProvider(DataProvider dataProvider) {
        this.originalDataProvider = dataProvider;
        return this;
    }

    public WorkExecutor doUI(Executor<?, ?> executor) {
        Worker<?, ?> worker = new Worker(getUiHandler(), executor, this);
        workerList.addLast(worker);
        return this;
    }

    public WorkExecutor doBackground(Executor<?, ?> executor) {
        Worker<?, ?> worker = new Worker(backgroundHandler, executor, this);
        workerList.addLast(worker);
        return this;
    }

    public WorkExecutor doException(ExceptionWorker exceptionWorker) {
        this.exceptionWorker = exceptionWorker;
        return this;
    }

    public void execute() {
        if (workerList.size() > 0) {
            Worker<?, ?> worker = workerList.removeFirst();
            worker.work(originalDataProvider);
        } else {
            finish();
        }
    }

    public void cancel() {
        for (Worker worker : workerList) {
            worker.cancelWork();
        }
    }

    private Handler getUiHandler() {
        Handler handler = uiHandler.get();
        if (handler == null) {
            uiHandler = new WeakReference<>(new Handler());
            handler = uiHandler.get();
        }
        return handler;
    }

    @Override
    public void receiveWorkSuccess(DataProvider dataProvider) {
        if (workerList.size() > 0) {
            Worker<?, ?> worker = workerList.removeFirst();
            worker.work(dataProvider);
        } else {
            finish();
        }
    }

    @Override
    public void receiveWorkCancel() {
        workerList.clear();
        finish();
    }

    @Override
    public void receiveWorkFail(Exception exception) {
        workerList.clear();
        if (exceptionWorker != null) {
            exceptionWorker.workException(exception);
        }
        finish();
    }

    private void finish() {
        workThreadHandler.quitHandler();
    }

}
