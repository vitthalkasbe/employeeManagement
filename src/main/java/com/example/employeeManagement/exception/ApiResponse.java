package com.example.employeeManagement.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse {

    private String message;
    private String detail;
    private LocalDateTime localDateTime;

    public ApiResponse()
    {

    }

}
