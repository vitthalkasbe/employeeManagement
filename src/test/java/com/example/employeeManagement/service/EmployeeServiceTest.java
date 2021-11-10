package com.example.employeeManagement.service;


import com.example.employeeManagement.entity.Employee;
import com.example.employeeManagement.respository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;
    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    public void shouldReturnEmployeeList() {
        List<Employee> givenEmployeeList = Arrays.asList(new Employee(1L, "vitthal", "kasbe", "vitthalkasbe@okvks", null),
                new Employee(2L, "vikas", "kamble", "vilaskamble@okvks", null));

        Mockito.when(employeeRepository.findAll()).thenReturn(givenEmployeeList);

        List<Employee> actualEmployees = employeeService.getEmployees();

        assertEquals(2, actualEmployees.size());
    }

    @Test
    public void shouldReturnEmployee() {
        Employee givenEmployee = new Employee(1L, "vitthal", "kasbe", "vitthalkasbe@okvks", null);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(givenEmployee));
        Optional<Employee> optionalEmployeeId = employeeService.findEmployeeById(1L);
        Employee actualEmployee = optionalEmployeeId.get();

        assertEquals("vitthal", actualEmployee.getFirstName());
        assertEquals("kasbe", actualEmployee.getLastName());
        assertEquals("vitthalkasbe@okvks", actualEmployee.getEmailId());

    }

    @Test
    public void shouldThrowExceptionWhenEmployeeNotFound() {
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertEquals(Optional.empty(), employeeService.findEmployeeById(1L));

    }

    @Test
    public void shouldReturnEmployeeWithGivenId() {
        Employee givenEmployee = new Employee(1L, "vitthal", "kasbe", "vitthalkasbe@okvks", null);
        Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(givenEmployee));

        Employee actualEmployee = employeeService.findEmployeeById(1L).get();

        assertEquals("vitthal", actualEmployee.getFirstName());
    }


}
