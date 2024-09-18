package com.example.employee.service;

import com.example.employee.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    EmployeeDTO getByEmployeeId(long employeeId);
    List<EmployeeDTO> getAllEmployee();
    EmployeeDTO addEmployee(EmployeeDTO employee);
    EmployeeDTO updateEmployee(EmployeeDTO employee, long id);
    void deleteEmployee(long id);
}
