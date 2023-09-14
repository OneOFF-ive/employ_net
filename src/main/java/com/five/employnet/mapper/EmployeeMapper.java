package com.five.employnet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.five.employnet.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
