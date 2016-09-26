package com.johan.workexecutor;

/**
 * Created by Administrator on 2016/9/26.
 */
public interface WorkResultReceiver {

    void receiveWorkSuccess(DataProvider dataProvider);

    void receiveWorkCancel();

    void receiveWorkFail(Exception exception);

}
