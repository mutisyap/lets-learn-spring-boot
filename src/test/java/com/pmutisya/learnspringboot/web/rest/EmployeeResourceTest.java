package com.pmutisya.learnspringboot.web.rest;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.pmutisya.learnspringboot.entity.Employee;
import com.pmutisya.learnspringboot.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeResource.class)
public class EmployeeResourceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void createEmployeeShouldReturnCreatedEmployee() throws Exception {
        // let's condition our service to perform as we expect
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("John");
        employee.setCompany("Meliora");

        given(employeeService.create(any())).willReturn(employee);

        ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        String employeeJson = objectMapper.writeValueAsString(employee);

        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(employeeJson));

    }

    @Test
    void createEmployeeShouldReturnCreatedEmployee2(@Autowired WebTestClient webTestClient) throws Exception {
        // let's condition our service to perform as we expect
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("John");
        employee.setCompany("Meliora");

        given(employeeService.create(any())).willReturn(employee);

        ObjectMapper objectMapper = new ObjectMapper().configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        String employeeJson = objectMapper.writeValueAsString(employee);


        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(employeeJson))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(employeeJson));

    }
}
