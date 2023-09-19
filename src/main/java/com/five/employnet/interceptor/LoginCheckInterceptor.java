package com.five.employnet.interceptor;

import com.alibaba.fastjson.JSON;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    JwtUtil jwtUtil;
    public LoginCheckInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截到请求:{}", request.getRequestURI());

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            log.info("请求{}通过", request.getRequestURI());
            // 使用Bearer令牌认证，提取令牌部分
            String authToken = authorizationHeader.substring(7); // 去掉"Bearer "前缀

            // 在这里可以对令牌进行JWT解析等
            // 如果验证成功，可以继续处理请求；如果验证失败，返回适当的响应
            if (jwtUtil.validateToken(authToken)) {
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
    }
}
