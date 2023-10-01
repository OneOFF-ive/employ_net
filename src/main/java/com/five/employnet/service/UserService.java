package com.five.employnet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.five.employnet.entity.User;

public interface UserService extends IService<User> {
    boolean transfer(User sourceUser, User targetUser);
}
