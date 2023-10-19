package com.five.employnet.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.five.employnet.common.R;
import com.five.employnet.config.CorsConfig;
import com.five.employnet.entity.Msg;
import com.five.employnet.service.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/msg")
@CrossOrigin(origins = CorsConfig.origins, allowCredentials = "true")
public class MsgController {

    final private MsgService msgService;

    public MsgController(MsgService msgService) {
        this.msgService = msgService;
    }

    @PostMapping
    public R<Msg> save(@RequestBody Msg msg) {
        msgService.save(msg);
        return R.success(msg);
    }

    @PutMapping
    public R<Msg> update(@RequestBody Msg msg) {
        if (msg.getId() != null) {
            msgService.updateById(msg);
            return R.success(msg);
        } else return R.error("id不正确");
    }

    @GetMapping
    public R<List<Msg>> getAllMsg() {
        List<Msg> msgList = msgService.list();
        return R.success(msgList);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam("id") String id) {
        msgService.removeById(id);
        return R.success("删除成功");
    }

    @GetMapping("/page")
    public R<Page<Msg>> page(@RequestParam int page, @RequestParam int pageSize) {
        Page<Msg> msgPage = new Page<>(page, pageSize);
        msgService.page(msgPage);
        return R.success(msgPage);
    }
}
