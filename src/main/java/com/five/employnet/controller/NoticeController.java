package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.five.employnet.common.R;
import com.five.employnet.config.CorsConfig;
import com.five.employnet.entity.Notice;
import com.five.employnet.entity.NoticeMessage;
import com.five.employnet.service.NoticeMessageService;
import com.five.employnet.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notice")
@CrossOrigin(origins = CorsConfig.origins, allowCredentials = "true")
public class NoticeController {

    final private NoticeService noticeService;
    final private NoticeMessageService noticeMessageService;

    public NoticeController(NoticeService noticeService, NoticeMessageService noticeMessageService) {
        this.noticeService = noticeService;
        this.noticeMessageService = noticeMessageService;
    }

    @PostMapping("/save")
    public R<Notice> save(@RequestBody Notice notice) {
        noticeService.saveOneNotice(notice);
        return R.success(notice);
    }

    @GetMapping("/page")
    public R<Page<Notice>> getPage(@RequestParam int page, @RequestParam int pageSize) {
        Page<Notice> noticePage = new Page<>(page, pageSize);
        noticeService.page(noticePage);

        List<Notice> noticeList = noticePage.getRecords();
        for (Notice notice : noticeList) {
            noticeService.completeNotice(notice);
        }
        return R.success(noticePage);
    }

    @GetMapping("/search")
    public R<Page<NoticeMessage>> search(@RequestParam int page, @RequestParam int pageSize, String prompt) {
        Page<NoticeMessage> messagePage = new Page<>(page, pageSize);
        LambdaQueryWrapper<NoticeMessage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .like(prompt != null, NoticeMessage::getLab, prompt)
                .or()
                .like(prompt != null, NoticeMessage::getTitle, prompt);
        noticeMessageService.page(messagePage, queryWrapper);
        return R.success(messagePage);
    }

    @DeleteMapping("/delete")
    public R<String> deleteByIds(@RequestParam List<Long> ids) {
        noticeService.removeByIds(ids);
        return R.success("删除成功");
    }
}
