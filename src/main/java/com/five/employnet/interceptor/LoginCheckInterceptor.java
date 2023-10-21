package com.five.employnet.interceptor;

import com.five.employnet.common.BaseContext;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.entity.Visitor;
import com.five.employnet.service.VisitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    JwtUtil jwtUtil;
    final VisitorService visitorService;

    public LoginCheckInterceptor(JwtUtil jwtUtil, VisitorService visitorService) {
        this.jwtUtil = jwtUtil;
        this.visitorService = visitorService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截到请求:{} {}", request.getMethod(), request.getRequestURI());

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // 使用Bearer令牌认证，提取令牌部分
            String authToken = authorizationHeader.substring(7); // 去掉"Bearer "前缀

            // 在这里可以对令牌进行JWT解析等
            // 如果验证成功，可以继续处理请求；如果验证失败，返回适当的响应
            if (jwtUtil.validateToken(authToken)) {
                log.info("请求{}通过", request.getRequestURI());
                String userId = jwtUtil.extractUserId(authToken);
                visitorService.addVisitor(new Visitor(userId, request.getRemoteAddr(), LocalDateTime.now()));
                BaseContext.setCurrentId(userId);
                return true; // 继续处理请求
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false; // 验证失败，中止请求处理
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false; // 缺少身份验证信息，返回401 Unauthorized
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        log.info("请求结束:{}", request.getRequestURI());
        BaseContext.removeCurrentId();
    }
}
