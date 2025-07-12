package com.neu.project.config;

import com.neu.project.handler.OnlineUserHandler;
import com.neu.project.interceptor.UserIdHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final OnlineUserHandler onlineUserHandler;

    public WebSocketConfig(OnlineUserHandler onlineUserHandler) {
        this.onlineUserHandler = onlineUserHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(onlineUserHandler, "/ws/online-users")
                .setAllowedOrigins("*")
                .setHandshakeHandler(new UserIdHandshakeInterceptor()); // 添加拦截器
    }
}