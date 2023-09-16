package com.five.employnet.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.five.employnet.common.JwtUtil;
import com.five.employnet.common.R;
import com.five.employnet.entity.Employee;
import com.five.employnet.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

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
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        if (emp != null && Objects.equals(emp.getPassword(), password)) {   //用户存在且密码正确
            String token = jwtUtil.generateToken(emp.getUsername());
            return R.success(token);
        }
        else {
            return R.error("0");
        }
    }

    @PostMapping("/register")
    R<String> register(@RequestBody Employee employee) {
        String username = employee.getUsername();
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, username);
        Employee emp = employeeService.getOne(queryWrapper);
        if (emp != null) {
            return R.error("0");    //用户已存在
        }
        else {
            String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes());
            employee.setPassword(password);
            employeeService.save(employee); //保存用户
            return R.success("1");
        }
    }

    @GetMapping("/test")
    R<String> test() {
        return R.success("success");
    }
}
