package com.example.employeeManagement.controller;

import com.example.employeeManagement.entity.Employee;
import com.example.employeeManagement.exception.EmployeeNotFoundException;
import com.example.employeeManagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController

public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getEmployees() {
        List<Employee> employees = employeeService.getEmployees();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id) {
        Optional<Employee> employeeOptional = employeeService.findEmployeeById(id);
        if (employeeOptional.isEmpty())
            throw new EmployeeNotFoundException("employee is not found with given id");

        return ResponseEntity.ok(employeeOptional.get());
    }

    @PostMapping("/employees")
    public ResponseEntity<Object> saveEmployee(@RequestBody @Valid  Employee employee) {
        System.out.println(employee.getFirstName()+" "+employee.getLastName());
        Employee savedEmployee = employeeService.saveEmployee(employee);

        return ResponseEntity.created(UriComponentsBuilder.fromPath("/employees/{id}").build(savedEmployee.getId())).build();
       // return new ResponseEntity<Object>(savedEmployee,HttpStatus.CREATED).
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") Long id, @RequestBody @Valid Employee employee) {
        Optional<Employee> employeeInDatabase = employeeService.findEmployeeById(id);
        if (employeeInDatabase.isEmpty())
            throw new EmployeeNotFoundException("employee is not found with given id");

        Employee updatedEmployee = employeeService.saveEmployee(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Object> deleteEmployeeById(@PathVariable("id") Long id) {
        employeeService.deleteEmployeeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
