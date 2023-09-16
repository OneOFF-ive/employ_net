package com.five.employnet.service;

import com.five.employnet.common.WeChatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeChatService {

    private final String WECHAT_API_URL = "https://api.weixin.qq.com/sns/jscode2session";
    private final String APP_ID = "your_app_id";
    private final String APP_SECRET = "your_app_secret";

    private final RestTemplate restTemplate;

    public WeChatService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public WeChatResponse getWeChatSessionInfo(String jsCode) {
        String url = WECHAT_API_URL + "?appid=" + APP_ID + "&secret=" + APP_SECRET + "&js_code=" + jsCode + "&grant_type=authorization_code";
        ResponseEntity<WeChatResponse> responseEntity = restTemplate.getForEntity(url, WeChatResponse.class);
        return responseEntity.getBody();
    }
}
