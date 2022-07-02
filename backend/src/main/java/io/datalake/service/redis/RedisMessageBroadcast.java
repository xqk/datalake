package io.datalake.service.redis;

public interface RedisMessageBroadcast<T> {

    void messageCallBack(T arg);
}
