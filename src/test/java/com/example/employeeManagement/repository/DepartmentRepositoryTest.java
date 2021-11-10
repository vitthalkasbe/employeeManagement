package com.example.employeeManagement.repository;

import com.example.employeeManagement.entity.Department;
import com.example.employeeManagement.respository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@ActiveProfiles("test")
public class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    public void shouldRetrieveAllDepartments()
    {
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(3);
    }

    @Test
    public void shouldSaveDepartment()
    {

        Department department = new Department(null, "ADM", "Application development and management",null);
        Department savedDepartment = departmentRepository.save(department);
        assertThat(department).usingRecursiveComparison().ignoringFields("id").isEqualTo(savedDepartment);
    }

    @Test
    public void shouldGetDepartmentById()
    {
        Optional<Department> optionalDepartment = departmentRepository.findById(100003L);
        Department department = optionalDepartment.get();
        assertThat(department).hasFieldOrPropertyWithValue("departmentName","BFS");
    }

    @Test
    public void shouldUpdateDepartment()
    {
        Department department = new Department(100001L, "Finance and Payroll", "Maintains finance", null);
        Department savedDepartment = departmentRepository.save(department);
        assertThat(savedDepartment).hasFieldOrPropertyWithValue("departmentName","Finance and Payroll");

    }

    @Test
    public void shouldDeleteDepartmentById()
    {
        departmentRepository.deleteById(100001L);
        List<Department> departmentList = departmentRepository.findAll();
        assertThat(departmentList).hasSize(2);
    }
}
