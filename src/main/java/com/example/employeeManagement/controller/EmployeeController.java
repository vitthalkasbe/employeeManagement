package com.example.employeeManagement.controller;

import com.example.employeeManagement.entity.Employee;
import com.example.employeeManagement.exception.EmployeeNotFoundException;
import com.example.employeeManagement.service.EmployeeService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController

public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    @ApiOperation(value = "Finds all employees from database", notes = "It finds all the employees from database", response = List.class)
    public ResponseEntity<List<Employee>> getEmployees() {
        List<Employee> employees = employeeService.getEmployees();
        employees.forEach(e -> {
            WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getEmployeeById(e.getId()));
            e.add(linkBuilder.withRel("employee-detail"));
        });
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employees/{id}")
    @ApiOperation(value = "find an employee by id", notes = "It finds employee with given id from employee management system", response = Employee.class)
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id) {
        Optional<Employee> employeeOptional = employeeService.findEmployeeById(id);
        if (employeeOptional.isEmpty())
            throw new EmployeeNotFoundException("employee is not found with given id");

        Employee employee = employeeOptional.get();
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getEmployees());
        employee.add(linkBuilder.withRel("all-employees"));

        return ResponseEntity.ok(employee);
    }

    @PostMapping("/employees")
    @ApiOperation(value = "saves employee", notes = "It saves employee provided by user to employee management system", response = Void.class)
    public ResponseEntity<Object> saveEmployee(@RequestBody @Valid Employee employee) {
        System.out.println(employee.getFirstName() + " " + employee.getLastName());
        Employee savedEmployee = employeeService.saveEmployee(employee);

        return ResponseEntity.created(UriComponentsBuilder.fromPath("/employees/{id}").build(savedEmployee.getId())).build();
        // return new ResponseEntity<Object>(savedEmployee,HttpStatus.CREATED).
    }

    @PutMapping("/employees/{id}")
    @ApiOperation(value = "update an employee by id", notes = "It updates an employee based upon id provided by user from user management system", response = Void.class)
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
