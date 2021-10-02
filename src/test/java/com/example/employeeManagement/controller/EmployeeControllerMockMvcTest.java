package com.example.employeeManagement.controller;

import com.example.employeeManagement.entity.Employee;
import com.example.employeeManagement.exception.EmployeeNotFoundException;
import com.example.employeeManagement.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerMockMvcTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    EmployeeService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getEmployeesTest() throws Exception {
        List<Employee> givenEmployeeList = Arrays.asList(new Employee(1L, "vitthal", "kasbe", "vitthalkasbe@okvks"),
                new Employee(2L, "vikas", "kamble", "vilaskamble@okvks"));

        when(service.getEmployees()).thenReturn(givenEmployeeList);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/employees")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("vitthal"));
    }

    @Test
    public void getEmployeeByIdTest() throws Exception {

        Employee employee1 = new Employee(1L, "vitthal", "kasbe", "vitthalkasbe@okvks");

        when(service.findEmployeeById(1L)).thenReturn(Optional.of(employee1));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/employees/{id}", 1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void saveEmployeeTest() throws Exception {
        Employee employee1 = new Employee(null, "vitthal", "kasbe", "vitthalkasbe@okvks");
        Employee employee2 = new Employee(1L, "vitthal", "kasbe", "vitthalkasbe@okvks");
        when(service.saveEmployee(any(Employee.class))).thenReturn(employee2);
        mockMvc.perform(MockMvcRequestBuilders.
                post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(employee1)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.containsString("/employees/1")));

    }

    @Test
    public void updateEmployeeTest() throws Exception {
        Employee employee2 = new Employee(2L, "vitthal", "kasbe", "vitthalkasbe@okvks");
        when(service.findEmployeeById(2L)).thenReturn(Optional.of(employee2));
        when(service.saveEmployee(any(Employee.class))).thenReturn(employee2);

        mockMvc.perform(MockMvcRequestBuilders.
                put("/employees/{id}",2).
                content(new ObjectMapper().writeValueAsString(employee2)).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
    }

    @Test
    public void updateEmployeeNegativeTest() throws Exception {
        Employee employee2 = new Employee(2L, "vitthal", "kasbe", "vitthalkasbe@okvks");
        when(service.findEmployeeById(3L)).thenReturn(Optional.empty());
        when(service.saveEmployee(any(Employee.class))).thenReturn(employee2);

        mockMvc.perform(MockMvcRequestBuilders.
                put("/employees/{id}",3).
                content(new ObjectMapper().writeValueAsString(employee2)).
                contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isNotFound());
    }

    @Test
    public void deleteEmployeeTest() throws Exception
    {
        doNothing().when(service).deleteEmployeeById(1L);
        mockMvc.perform(MockMvcRequestBuilders.delete("/employees/{id}",1))
                                              .andExpect(status().isOk());
    }

}
