package com.johan.workexecutor;

/**
 * Created by Administrator on 2016/9/26.
 */
public interface Executor<T, K> {

    K execute(T data);

}
