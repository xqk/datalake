package io.datalake.service.redis.impl;

import io.datalake.service.redis.RedisMessageBroadcast;
import org.springframework.stereotype.Service;

@Service
public class PluginMsgService implements RedisMessageBroadcast {

    @Override
    public void messageCallBack(Object arg) {

    }
}
