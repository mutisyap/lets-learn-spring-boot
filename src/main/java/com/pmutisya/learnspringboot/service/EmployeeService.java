package com.pmutisya.learnspringboot.service;

import com.pmutisya.learnspringboot.entity.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {
    private final Map<Integer, Employee> employeeMap = new HashMap<>();

    public Employee create(Employee employee) {
        employeeMap.put(employee.getId(), employee);
        return employee;
    }

    public Employee update(Employee employee, Integer id) {
        employeeMap.put(id, employee);
        return employee;
    }

    public Employee read(Integer id) {
        return employeeMap.get(id);
    }

    public List<Employee> readAll() {
        return new ArrayList<>(employeeMap.values());
    }

    public void delete(Integer id) {
        employeeMap.remove(id);
    }
}
