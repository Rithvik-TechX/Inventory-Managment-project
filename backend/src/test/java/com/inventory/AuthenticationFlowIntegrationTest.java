package com.inventory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:auth-flow;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
        "jwt.secretKey=0123456789abcdef0123456789abcdef0123456789abcdef0123456789abcdef",
        "spring.mail.username=test@example.com",
        "spring.mail.password=test"
})
@AutoConfigureMockMvc
class AuthenticationFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void defaultAdminCanLoginReceiveJwtAndAccessProtectedApi() throws Exception {
        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"username":"admin@inventory.com","password":"admin123"}
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode body = objectMapper.readTree(response);
        String jwt = body.path("jwt").asText();

        assertThat(jwt).isNotBlank();
        assertThat(body.path("role").asText()).isEqualTo("ADMIN");

        mockMvc.perform(get("/api/products")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/categories").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[0].productCount").isNumber());

        mockMvc.perform(get("/api/suppliers").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].active").exists());

        mockMvc.perform(get("/api/orders").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/search?q=wireless").header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].name").value("Wireless Mouse"))
                .andExpect(jsonPath("$.products[0].sku").value("ELEC-002"));
    }
}
