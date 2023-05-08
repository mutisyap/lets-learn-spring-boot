package com.pmutisya.learnspringboot.service;

import com.pmutisya.learnspringboot.entity.Employee;
import com.pmutisya.learnspringboot.repository.EmployeeRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee create(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee update(Employee employee, Integer id) {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> read(Integer id) {
        return employeeRepository.findById(id);
    }

    public List<Employee> readAll() {
        return employeeRepository.findAll();
    }

    public void delete(Integer id) {
        employeeRepository.deleteById(id);
    }
}
