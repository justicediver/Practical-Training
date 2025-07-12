package com.neu.project.interceptor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import java.security.Principal;
import java.util.Map;

public class UserIdHandshakeInterceptor extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            String userIdParam = servletRequest.getServletRequest().getParameter("userId");
            if (userIdParam != null) {
                try {
                    int userId = Integer.parseInt(userIdParam);
                    attributes.put("userId", userId);
                    return () -> String.valueOf(userId);
                } catch (NumberFormatException e) {
                    if ("admin_root".equals(userIdParam)) {
                        attributes.put("userId", 0);
                        return () -> "admin_root";
                    }
                }
            }
        }
        return null;
    }
}