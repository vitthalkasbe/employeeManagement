package com.example.employeeManagement.controller;

import com.example.employeeManagement.entity.Department;
import com.example.employeeManagement.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerMockMvcTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    DepartmentService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetDepartments() throws Exception {

        List<Department> departments = Arrays.asList(new Department(1l, "my dept", "my dept", null), new Department(2l, "test dept", "tes dept", null));
        Mockito.when(service.getDepartments()).thenReturn(departments);

        mockMvc.perform(MockMvcRequestBuilders.get("/departments").accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic dXNlcjpwYXNzd29yZA=="))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].departmentName").value("my dept"));
    }

    @Test
    public void testGetDepartmentById() throws Exception {
        Department department = new Department(1L, "My department", "my department", null);
        Mockito.when(service.getDepartmentByID(1L)).thenReturn(Optional.of(department));

        mockMvc.perform(MockMvcRequestBuilders.get("/departments/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic dXNlcjpwYXNzd29yZA=="))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

    }

    @Test
    public void testSaveDepartment() throws Exception {
        Department department = new Department(1L, "My department", "my department", null);

        Mockito.when(service.saveDepartment(department)).thenReturn(department);

        mockMvc.perform(MockMvcRequestBuilders.post("/departments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(department))
                .header("Authorization", "Basic dXNlcjpwYXNzd29yZA=="))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("location", Matchers.containsString("/departments/1")));
    }

    @Test
    public void testUpdateDepartement() throws Exception {
        Department department = new Department(1L, "My department", "my department", null);
        Mockito.when(service.getDepartmentByID(1L)).thenReturn(Optional.of(department));
        Mockito.when(service.saveDepartment(department)).thenReturn(department);

        mockMvc.perform(MockMvcRequestBuilders.put("/departments/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(department))
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic dXNlcjpwYXNzd29yZA=="))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void testDeleteDepartment() throws Exception {
        Mockito.doNothing().when(service).deleteDepartment(1l);

        mockMvc.perform(MockMvcRequestBuilders.delete("/departments/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Basic dXNlcjpwYXNzd29yZA=="))
                .andExpect(status().isOk());
    }

}
