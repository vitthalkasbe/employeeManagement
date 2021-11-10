package com.example.employeeManagement.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ApiModel("Employee details")
public class Employee extends RepresentationModel<Employee> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("unique ID of employee")
    private Long id;


    @NotBlank(message = "firstname should not be blank or null")
    @ApiModelProperty("First Name of the employee")
    private String firstName;

    @NotBlank(message = "lastname should not be blank or null ")
    @ApiModelProperty("Last Name of the employee")
    private String lastName;

    @NotNull
    @ApiModelProperty("Email ID of the employee")
    private String emailId;

    @ManyToOne
    @JoinColumn(name = "dept_id",referencedColumnName = "id")
    @JsonBackReference
    private Department department;

}
