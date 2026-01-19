package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Department;
import com.example.demo.projection.EmployeeProjection;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentService departmentService;

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee createEmployeeWithDepartment(Employee employee, Long departmentId) {
        Department department = departmentService.getDepartmentById(departmentId);
        employee.setDepartment(department);
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }


    public List<EmployeeProjection> getAllEmployeesProjection() {
        return employeeRepository.findAllProjectedBy();
    }


    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Сотрудник не найден с id: " + id));
    }

    public EmployeeProjection getEmployeeProjectionById(Long id) {
        return employeeRepository.findProjectedById(id);
    }

    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Employee employee = getEmployeeById(id);

        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        employee.setPosition(employeeDetails.getPosition());
        employee.setSalary(employeeDetails.getSalary());

        if (employeeDetails.getDepartment() != null) {
            employee.setDepartment(employeeDetails.getDepartment());
        }

        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}