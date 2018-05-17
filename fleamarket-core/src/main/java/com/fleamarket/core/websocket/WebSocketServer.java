package com.fleamarket.core.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fleamarket.core.model.Letter;
import com.fleamarket.core.service.LetterService;
import com.fleamarket.core.util.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
@ServerEndpoint("/user/ws")
public class WebSocketServer {
    private Map<Session, Integer> wsUserIdMap = new HashMap<>(); // 保存有当前在聊天窗口的session
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final LetterService letterService;

    @Autowired
    public WebSocketServer(LetterService letterService) {
        this.letterService = letterService;
    }

    @OnOpen
    public void onOpen(Session session) {
        log.debug("User(id="+Utils.getUserSession().getId()+") websocket open.");
        wsUserIdMap.put(session, Utils.getUserSession().getId());
    }

    /**
     * 客户端发来数据时
     *
     * @param messageJson e.g. {"target": 1012, "content": "hello"}
     * @param session
     */
    @OnMessage
    public void onMessage(String messageJson, Session session) {
        try {
            MessageWrap messageWrap = objectMapper.readValue(messageJson, MessageWrap.class);
            Integer targetUserId = messageWrap.getTarget();
            Integer sourceUserId = wsUserIdMap.get(session);
            String msg = String.valueOf(messageWrap.getContent());
            log.debug("User(id=" + sourceUserId + ") -> User(id=" + targetUserId + ") msg=" + msg);
            if (letterService.insertSelective(new Letter(sourceUserId, targetUserId, msg))) {
                wsUserIdMap.forEach((k, v) -> {
                    if (v.equals(targetUserId)) {
                        try {
                            k.getBasicRemote().sendText(objectMapper.writeValueAsString(new MessageWrap(sourceUserId, msg)));
                        } catch (IOException e) {
                            log.error(e.getMessage());
                        }
                    }
                });
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session) {
        wsUserIdMap.remove(session);
        log.debug("User(id=" + wsUserIdMap.get(session) + ") websocket close.");
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("User(id=" + wsUserIdMap.get(session) + ") websocket error.", error);
    }

    @AllArgsConstructor
    @Setter
    @Getter
    private static class MessageWrap {
        private Integer target;
        private String content;
    }
}
