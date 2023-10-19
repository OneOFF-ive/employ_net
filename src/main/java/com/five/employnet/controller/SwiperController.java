package com.five.employnet.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.five.employnet.common.R;
import com.five.employnet.entity.Swiper;
import com.five.employnet.service.SwiperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/swiper")
public class SwiperController {
    final private SwiperService swiperService;

    public SwiperController(SwiperService swiperService) {
        this.swiperService = swiperService;
    }

    @GetMapping
    public R<List<Swiper>> getAllSwiper() {
        List<Swiper> swiperList = swiperService.list();
        return R.success(swiperList);
    }

    @PutMapping
    public R<Swiper> updateSwiper(@RequestBody Swiper swiper) {
        if (swiper.getId() != null) {
            swiperService.updateById(swiper);
            return R.success(swiper);
        } else {
            return R.error("不正确的id");
        }
    }

    @DeleteMapping
    public R<String> deleteSwiper(@RequestParam("id") String id) {
        swiperService.removeById(id);
        return R.success("删除成功");
    }

    @PostMapping
    public R<Swiper> saveSwiper(@RequestBody Swiper swiper) {
        swiperService.save(swiper);
        return R.success(swiper);
    }

    @GetMapping("/page")
    public R<Page<Swiper>> page(@RequestParam int page, @RequestParam int pageSize) {
        Page<Swiper> swiperPage = new Page<>(page, pageSize);
        swiperService.page(swiperPage);
        return R.success(swiperPage);
    }
}
