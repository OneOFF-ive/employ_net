package com.five.employnet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.five.employnet.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
