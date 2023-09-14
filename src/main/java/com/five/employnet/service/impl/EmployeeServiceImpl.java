package com.five.employnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.five.employnet.entity.Employee;
import com.five.employnet.mapper.EmployeeMapper;
import com.five.employnet.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
