package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.common.R;
import com.five.employnet.entity.Employee;
import com.five.employnet.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeControl {
    private final EmployeeService employeeService;
    private final JwtUtil jwtUtil;

    public EmployeeControl(EmployeeService employeeService, JwtUtil jwtUtil) {
        this.employeeService = employeeService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    R<String> login(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.getUsername());
        String password = employee.getPassword();
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        if (emp != null) {
            String token = jwtUtil.generateToken(emp.getUsername());
            return R.success(token);
        }
        else {
            return R.error("0");
        }
    }

    @GetMapping("/test")
    R<String> test() {
        return R.success("success");
    }
}
