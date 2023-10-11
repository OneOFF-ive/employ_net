package com.five.employnet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * 注入一个ServerEndpointExporter,该Bean会自动注册使用@ServerEndpoint注解申明的websocket endpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 指定WebSocket端点
        registry.addEndpoint("/im/{username}")
                .setAllowedOrigins("*")
                .setAllowedOriginPatterns("*")
                .withSockJS(); // 启用SocketJS支持
    }
}
