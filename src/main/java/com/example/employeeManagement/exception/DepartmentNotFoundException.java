package com.example.employeeManagement.exception;

public class DepartmentNotFoundException extends RuntimeException {

    public DepartmentNotFoundException()
    {
    }
    public DepartmentNotFoundException(String message)
    {
        super(message);
    }
}
