package com.example.demo.service;

import com.example.demo.projection.EmployeeProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    public List<EmployeeProjection> getAllEmployeesInfo() {
        return employeeService.getAllEmployeesProjection();
    }
}