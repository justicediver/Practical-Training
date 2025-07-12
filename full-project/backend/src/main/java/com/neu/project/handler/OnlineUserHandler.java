package com.neu.project.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neu.project.entity.User;
import com.neu.project.mapper.UserMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OnlineUserHandler extends TextWebSocketHandler {

    private final UserMapper userMapper;
    private static final Map<Integer, WebSocketSession> onlineUsers = new ConcurrentHashMap<>();

    public OnlineUserHandler(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Integer userId = (Integer) session.getAttributes().get("userId");
        if (userId != null) {
            // 特殊处理管理员ID=0
            if (userId == 0) {
                // 检查管理员用户是否存在
                User admin = userMapper.findUserById(0);
                if (admin == null) {
                    // 创建虚拟管理员用户
                    admin = new User();
                    admin.setId(0);
                    admin.setUsername("root");
                }
            }
            onlineUsers.put(userId, session);
            broadcastOnlineUsers();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Integer userId = (Integer) session.getAttributes().get("userId");
        if (userId != null) {
            onlineUsers.remove(userId);
            broadcastOnlineUsers();
        }
    }

    private void broadcastOnlineUsers() throws IOException {
        List<Map<String, Object>> onlineUserList = new ArrayList<>();
        for (Integer userId : onlineUsers.keySet()) {
            User user = userMapper.findUserById(userId);
            if (user != null) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", user.getId());
                userInfo.put("username", user.getUsername());
                onlineUserList.add(userInfo);
            }
        }

        // 使用JSON序列化
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(onlineUserList);

        for (WebSocketSession session : onlineUsers.values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(json));
            }
        }
    }

    public static Map<Integer, WebSocketSession> getOnlineUsers() {
        return Collections.unmodifiableMap(onlineUsers);
    }
}