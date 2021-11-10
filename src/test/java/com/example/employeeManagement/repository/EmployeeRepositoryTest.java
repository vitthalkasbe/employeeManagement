package com.example.employeeManagement.repository;

import com.example.employeeManagement.entity.Department;
import com.example.employeeManagement.entity.Employee;
import com.example.employeeManagement.respository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void shouldSaveEmployee() {
        Employee employee = new Employee(null, "vitthal", "kasbe", "vitthalkasbe@okvks", null);
        Department department = new Department(100001L, "Finance and Payroll", "Maintains finance", null);
        employee.setDepartment(department);
        Employee savedEmployee = employeeRepository.save(employee);
        assertThat(employee).usingRecursiveComparison().ignoringFields("id").isEqualTo(savedEmployee);
    }

    @Test
    public void shouldRetrieveAllEmployees() {
        List<Employee> employeeList = employeeRepository.findAll();

        assertThat(employeeList.size()).isEqualTo(5);
    }

    @Test
    public void shouldRetrieveEmployeeById() {
        Optional<Employee> employee = employeeRepository.findById(10001L);
        assertThat(employee.get().getEmailId()).isEqualTo("vitthalkasbe@outlook.com");
    }

    @Test
    public void shouldUpdateEmployee() {
        Employee employee = new Employee(10001L, "vitthalkasbe@outlook.com", "akash", "kasbe", null);
        Employee savedEmployee = employeeRepository.save(employee);
        assertThat(savedEmployee).hasFieldOrPropertyWithValue("lastName", "akash");
    }

    @Test
    public void shouldDeleteEmployeeById() {
        employeeRepository.deleteById(10001L);
        List<Employee> employeeList = employeeRepository.findAll();
        assertThat(employeeList).hasSize(4);
    }
}
