package com.five.employnet.service;

import com.five.employnet.common.WeChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class WeChatService {
    @Value("${wechat.app-url}")
    private String WECHAT_API_URL;
    @Value("${wechat.app-id}")
    private String APP_ID;
    @Value("${wechat.app-secret}")
    private String APP_SECRET;

    private final RestTemplate restTemplate;

    public WeChatService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JSONObject getWeChatSessionInfo(String jsCode) {
        String url = WECHAT_API_URL + "?appid=" + APP_ID + "&secret=" + APP_SECRET + "&js_code=" + jsCode + "&grant_type=authorization_code";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String responseBody = responseEntity.getBody();
        return new JSONObject(responseBody);
    }

    public String test(String jsCode) {
        String url = WECHAT_API_URL + "?appid=" + APP_ID + "&secret=" + APP_SECRET + "&js_code=" + jsCode + "&grant_type=authorization_code";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String responseBody = responseEntity.getBody();
        JSONObject jsonResponse = new JSONObject(responseBody);

        return responseEntity.getBody();
    }
}
