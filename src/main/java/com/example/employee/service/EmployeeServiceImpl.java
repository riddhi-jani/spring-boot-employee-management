package com.example.employee.service;

import com.example.employee.dto.EmployeeDTO;
import com.example.employee.entity.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "employees")
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    @Cacheable(key = "#employeeId")
    public EmployeeDTO getByEmployeeId(long employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        Employee employee = employeeOptional.orElse(null);
        if (employee != null) {
            return convertToDTO(employee);
        } else {
            return null;
        }

    }

    @Override
    @Cacheable(value = "employees")
    public List<EmployeeDTO> getAllEmployee() {
        List<Employee> employeeList = employeeRepository.findAll();
        return employeeList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    @Cacheable()
    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.save(convertToEntity(employeeDTO));
        employeeDTO.setEmployeeId(employee.getEmployeeId());
        return employeeDTO;
    }

    @Override
    @CachePut(key = "#employeeId")
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO, long employeeId) {
        employeeDTO.setEmployeeId(employeeId);
        employeeRepository.save(convertToEntity(employeeDTO));
        return employeeDTO;
    }

    @Override
    @CacheEvict( key = "#employeeId")
    public void deleteEmployee(long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }

    private Employee convertToEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        return employee;
    }
}
