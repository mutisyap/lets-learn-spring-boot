package com.pmutisya.learnspringboot.web.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HelloController.class)
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testSayHello() throws Exception {
        mockMvc.perform(get("/"))
                .andExpectAll(
                        status().isOk(),
                        content().string("Hello World"),
                        content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN)
                );

    }
}
