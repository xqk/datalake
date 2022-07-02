package io.datalake.service.redis.impl;

import com.google.gson.Gson;
import io.datalake.service.redis.RedisMessageBroadcast;
import io.datalake.websocket.entity.WsMessage;
import io.datalake.websocket.service.impl.StandaloneWsService;
import io.datalake.websocket.util.WsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class WsMsgService implements RedisMessageBroadcast<Map> {

    private static Gson json = new Gson();

    @Autowired
    private StandaloneWsService standaloneWsService;

    @Override
    public void messageCallBack(Map arg) {
        WsMessage message = json.fromJson(json.toJson(arg), WsMessage.class);
        Long userId = message.getUserId();
        if (WsUtil.isOnLine(userId)) {
            standaloneWsService.releaseMessage(message);
        }
    }
}
