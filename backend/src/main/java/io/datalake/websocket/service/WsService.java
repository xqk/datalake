package io.datalake.websocket.service;

import io.datalake.websocket.entity.WsMessage;


public interface WsService {

    void releaseMessage(WsMessage wsMessage);


}
