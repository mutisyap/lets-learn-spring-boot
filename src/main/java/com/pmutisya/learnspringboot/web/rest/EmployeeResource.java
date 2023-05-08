package com.pmutisya.learnspringboot.web.rest;

import com.pmutisya.learnspringboot.entity.Employee;
import com.pmutisya.learnspringboot.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Employee> read(@PathVariable Integer id) {

        Optional<Employee> optional = employeeService.read(id);

        return optional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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
