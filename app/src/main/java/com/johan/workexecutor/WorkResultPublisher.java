package com.johan.workexecutor;

/**
 * Created by Administrator on 2016/9/26.
 */
public interface WorkResultPublisher {

    void publishWorkSuccess(WorkResultReceiver receiver, DataProvider dataProvider);

    void publishWorkCancel(WorkResultReceiver receiver);

    void publishWorkFail(WorkResultReceiver receiver, Exception exception);

}
