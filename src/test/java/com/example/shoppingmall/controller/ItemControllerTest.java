package com.example.shoppingmall.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
class ItemControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("삼풍 등록 페이지 ADMIN 확인")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void itemFormAdminTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(System.out::println)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    @DisplayName("삼풍 등록 페이지 User 확인")
    @WithMockUser(username = "user", roles = "USER")
    public void itemFormUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))
                .andDo(System.out::println)
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}