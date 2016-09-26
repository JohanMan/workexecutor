package com.johan.workexecutor;

import android.os.Handler;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Administrator on 2016/9/26.
 */
public class Worker<T, K> implements WorkResultPublisher {

    private Handler handler;
    private Executor<T, K> executor;
    private WorkResultReceiver receiver;

    private AtomicBoolean isCancel;

    private long delay;

    public Worker(Handler handler, Executor<T, K> executor, WorkResultReceiver receiver) {
        this(handler, executor, 0, receiver);
    }

    public Worker(Handler handler, Executor<T, K> executor, long delay, WorkResultReceiver receiver) {
        this.handler = handler;
        this.executor = executor;
        this.delay = delay;
        this.receiver = receiver;
        this.isCancel = new AtomicBoolean(false);
    }

    public void work(final DataProvider<T> dataProvider) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isCancel.get()) {
                    publishWorkCancel(receiver);
                } else {
                    try {
                        T sourceData = dataProvider.getData();
                        K resultData = executor.execute(sourceData);
                        DataProvider<K> nextDataProvider = new DataProvider<>();
                        nextDataProvider.setData(resultData);
                        publishWorkSuccess(receiver, nextDataProvider);
                    } catch (Exception exception) {
                        publishWorkFail(receiver, exception);
                    }
                }
            }
        }, delay);
    }

    public void cancelWork() {
        this.isCancel.set(true);
    }

    @Override
    public void publishWorkSuccess(WorkResultReceiver receiver, DataProvider dataProvider) {
        receiver.receiveWorkSuccess(dataProvider);
    }

    @Override
    public void publishWorkCancel(WorkResultReceiver receiver) {
        receiver.receiveWorkCancel();
    }

    @Override
    public void publishWorkFail(WorkResultReceiver receiver, Exception exception) {
        receiver.receiveWorkFail(exception);
    }

}
