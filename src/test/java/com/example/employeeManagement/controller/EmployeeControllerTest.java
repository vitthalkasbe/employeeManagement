package com.example.employeeManagement.controller;

import com.example.employeeManagement.entity.Employee;
import com.example.employeeManagement.exception.EmployeeNotFoundException;
import com.example.employeeManagement.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController controller;
    @Mock
    private EmployeeService service;

    @Mock
    private HttpServletRequest request;

    @Mock
    private BindingResult mockBindingResult;

    @BeforeEach
    public void setUp() {
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void shouldReturnStatusOKWhenGetEmployees() {
        //given
        List<Employee> givenEmployeeList = Arrays.asList(new Employee(1L, "vitthal", "kasbe", "vitthalkasbe@okvks"),
                new Employee(2L, "vikas", "kamble", "vilaskamble@okvks"));

        //when
        when(service.getEmployees()).thenReturn(givenEmployeeList);

        //then
        assertEquals(HttpStatus.OK, controller.getEmployees().getStatusCode());
    }

    @Test
    public void shouldThrowEmployeeNotFoundExceptionWhenEmployeeIdIsNotFound() {
        when(service.findEmployeeById(1L)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class, () -> controller.getEmployeeById(1L));
    }

    @Test
    public void shouldReturnOKWhenEmployeeByIdIsPresent() {
        Employee employee = new Employee(1L, "vitthal", "kasbe", "vitthalkasbe@okvks");
        when(service.findEmployeeById(1L)).thenReturn(Optional.of(employee));
        assertEquals(HttpStatus.OK, controller.getEmployeeById(1L).getStatusCode());
    }

    @Test
    public void shouldReturnCreatedWhenSaveEmployeeCalled() {
        Employee givenEmployee = new Employee(1L, "vitthal", "kasbe", "vitthalkasbe@okvks");
        when(service.saveEmployee(givenEmployee)).thenReturn(givenEmployee);

        assertEquals(HttpStatus.CREATED, controller.saveEmployee(givenEmployee).getStatusCode());

    }

    @Test
    public void shouldReturnOKWhenEmployeeUpdated() {
        Employee givenEmployee = new Employee(1L, "vitthal", "kasbe", "vitthalkasbe@okvks");
        when(service.saveEmployee(givenEmployee)).thenReturn(givenEmployee);
        when(service.findEmployeeById(1L)).thenReturn(Optional.of(givenEmployee));
        assertEquals(HttpStatus.OK, controller.updateEmployee(1L, givenEmployee).getStatusCode());
    }

    @Test
    public void shouldThrowBindingErrorWhenWrongDataProvided() {
        Employee givenEmployee = new Employee(1L, null, null, "vitthalkasbe@okvks");
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        // Validate the object
        Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(givenEmployee);
        assertEquals(2, constraintViolations.size());


    }

    @Test
    public void shouldReturnOKOnDeleteEmployee() {

        doNothing().when(service).deleteEmployeeById(1L);

        assertEquals(HttpStatus.OK, controller.deleteEmployeeById(1L).getStatusCode());

    }
}
