package com.example.demo.controller;

import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.projection.EmployeeProjection;
import com.example.demo.service.DepartmentService;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CompanyController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/employees/projection")
    public ResponseEntity<List<EmployeeProjection>> getAllEmployeesProjection() {
        return ResponseEntity.ok(employeeService.getAllEmployeesProjection());
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/employees/{id}/projection")
    public ResponseEntity<EmployeeProjection> getEmployeeProjectionById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeProjectionById(id));
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.createEmployee(employee));
    }

    @PostMapping("/departments/{departmentId}/employees")
    public ResponseEntity<Employee> createEmployeeWithDepartment(
            @PathVariable Long departmentId,
            @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.createEmployeeWithDepartment(employee, departmentId));
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employeeDetails) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDetails));
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }

    @PostMapping("/departments")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        return ResponseEntity.ok(departmentService.createDepartment(department));
    }

    @PutMapping("/departments/{id}")
    public ResponseEntity<Department> updateDepartment(
            @PathVariable Long id,
            @RequestBody Department departmentDetails) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, departmentDetails));
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
}