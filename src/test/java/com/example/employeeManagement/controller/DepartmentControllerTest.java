package com.example.employeeManagement.controller;

import com.example.employeeManagement.entity.Department;
import com.example.employeeManagement.exception.DepartmentNotFoundException;
import com.example.employeeManagement.service.DepartmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class DepartmentControllerTest {

    @InjectMocks
    DepartmentController departmentController;

    @Mock
    DepartmentService departmentService;

    @Test
    public void shouldReturnOKWhenGetDepartments() {
        List<Department> departments = Arrays.asList(new Department(1L, "Finance", "Handles all finance", null));
        Mockito.when(departmentService.getDepartments()).thenReturn(departments);
        Assertions.assertEquals(HttpStatus.OK, departmentController.getAllDepartments().getStatusCode());
    }

    @Test
    public void shouldReturnOkWhenDepartmentByIDIsAvailable() {
        Department department = new Department(1L, "BFS", "Banking and fincial domain", null);
        Mockito.when(departmentService.getDepartmentByID(1L)).thenReturn(Optional.of(department));

        Assertions.assertEquals(HttpStatus.OK, departmentController.getDepartment(1L).getStatusCode());
    }

    @Test
    public void shouldThrowDepartmentNotFoundExceptionWhenDepartmentIDIsNotAvailable() {
        Mockito.when(departmentService.getDepartmentByID(1l)).thenThrow(new DepartmentNotFoundException("Department id is not present in db"));
        Assertions.assertThrows(DepartmentNotFoundException.class, () -> departmentController.getDepartment(1L));

    }

    @Test
    public void shouldReturnCreatedWhenDepartmentSaved() {
        Department department = new Department(1L, "BFS", "Banking and fincial domain", null);
        Mockito.when(departmentService.saveDepartment(department)).thenReturn(department);

        Assertions.assertEquals(HttpStatus.CREATED, departmentController.saveDepartment(department).getStatusCode());
    }


    @Test
    public void shouldReturnOKWhenUpdatedDepartment() {
        Department department = new Department(1L, "BFS", "Banking and fincial domain", null);
        Mockito.when(departmentService.getDepartmentByID(1L)).thenReturn(Optional.of(department));
        Mockito.when(departmentService.saveDepartment(department)).thenReturn(department);

        Assertions.assertEquals(HttpStatus.OK, departmentController.updateDepartment(department, 1L).getStatusCode());
    }


    @Test
    public void shouldReturnOKWhenDeletedDepartment() {
        Mockito.doNothing().when(departmentService).deleteDepartment(1L);
        Assertions.assertEquals(HttpStatus.OK, departmentController.deleteDepartmentById(1L).getStatusCode());
    }

    @Test
    public void shouldThrowBindingErrorsWhenWrongDataProvided() {
        Department department = new Department(1L, null, null, null);

        //validator factor
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        Set<ConstraintViolation<Department>> constraintViolations = validator.validate(department);
        Assertions.assertEquals(1, constraintViolations.size());
    }
}
