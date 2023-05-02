package com.pmutisya.learnspringboot.web.rest;

import com.pmutisya.learnspringboot.entity.Employee;
import com.pmutisya.learnspringboot.service.EmployeeService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeResource {

    private final EmployeeService employeeService;

    public EmployeeResource(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // create
    @PostMapping("/employees")
    public Employee create(@Validated @RequestBody Employee employee) {
        return employeeService.create(employee);
    }

    // update
    @PutMapping("/employees/{id}")
    public Employee update(@RequestBody Employee employee, @PathVariable Integer id) {
        return employeeService.update(employee, id);
    }

    // read - one
    @GetMapping("/employees/{id}")
    public Employee read(@PathVariable Integer id) {
        return employeeService.read(id);
    }

    // read - all
    @GetMapping("/employees")
    public List<Employee> readAll() {
        return employeeService.readAll();
    }

    // delete
    @DeleteMapping("/employees/{id}")
    public void delete(@PathVariable Integer id) {
        employeeService.delete(id);
    }
}
