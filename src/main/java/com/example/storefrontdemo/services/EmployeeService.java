package com.example.storefrontdemo.services;

import com.example.storefrontdemo.domain.entities.Employee;

public interface EmployeeService extends CRUDService<Employee>{

    Employee loginEmployee(String Username, String password);

    Employee findByUsername(String username);

    String setUpdatedFields(Employee passedInEmployee);

    String setUpdatedFields(Employee employee, Employee passedInEmployee);
}