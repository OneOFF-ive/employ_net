package com.five.employnet.controller;

import com.five.employnet.common.R;
import com.five.employnet.entity.Msg;
import com.five.employnet.service.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/msg")
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
}
