package com.five.employnet.config;

import com.five.employnet.common.JacksonObjectMapper;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.interceptor.LoginCheckInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;


@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    final JwtUtil jwtUtil;

    public WebMvcConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor(jwtUtil))
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login", "/company/login", "/common/*");
    }

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("扩展消息转换器");
        // 创建消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        // 设置对象转换器,使用Jackson将Java对象转换成Json
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        // 将自定义消息转换器放入MVC消息转换器集合中
        // 索引0，优先使用自定义的消息转换器
        converters.add(0, messageConverter);
    }
}
