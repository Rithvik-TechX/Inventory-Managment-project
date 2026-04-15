package com.inventory.server;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void supplierTokenCanAccessSupplierRouteButNotAdminRoute() throws Exception {
        MvcResult signupResult = mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "supplier_user",
                                  "password": "password123",
                                  "role": "SUPPLIER"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode signupPayload = objectMapper.readTree(signupResult.getResponse().getContentAsString());
        org.junit.jupiter.api.Assertions.assertEquals("SUPPLIER", signupPayload.get("role").asText());

        String loginResponse = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username": "supplier_user",
                                  "password": "password123"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode payload = objectMapper.readTree(loginResponse);
        org.junit.jupiter.api.Assertions.assertEquals("SUPPLIER", payload.get("role").asText());
        String token = payload.get("jwt").asText();

        mockMvc.perform(get("/supplier/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/admin/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}
