package com.example.employeeManagement.controller;

import com.example.employeeManagement.entity.Department;
import com.example.employeeManagement.exception.DepartmentNotFoundException;
import com.example.employeeManagement.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;

@RestController
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentService.getDepartments();
        departments.forEach(e ->
        {
            WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getDepartment(e.getId()));
            e.add(linkBuilder.withRel("employee-details"));
        });
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<Department> getDepartment(@PathVariable("id") Long id) {
        Optional<Department> optionalDepartment = departmentService.getDepartmentByID(id);

        if (!optionalDepartment.isPresent())
            throw new DepartmentNotFoundException("The given department is not found");
        Department department = optionalDepartment.get();
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).getAllDepartments());

        department.add(linkBuilder.withRel("all-departments"));
        return ResponseEntity.ok(department);

    }

    @PostMapping("/departments")
    public ResponseEntity<Object> saveDepartment(@RequestBody Department department) {
        Department savedDepartment = departmentService.saveDepartment(department);
        return ResponseEntity.created(UriComponentsBuilder.fromPath("/departments/{id}").build(savedDepartment.getId())).build();
    }

    @PutMapping("/departments/{id}")
    public ResponseEntity<Department> updateDepartment(@RequestBody Department department, @PathVariable("id") Long id) {
        Optional<Department> departmentInDB = departmentService.getDepartmentByID(id);
        if (departmentInDB.isEmpty())
            throw new DepartmentNotFoundException("The given department is not found");
        Department saveDepartment = departmentService.saveDepartment(department);

        return ResponseEntity.ok(saveDepartment);
    }


    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Object> deleteDepartmentById(@PathVariable("id") Long id) {
        departmentService.deleteDepartment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
