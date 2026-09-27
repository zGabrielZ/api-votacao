package br.com.gabrielferreira.votacao.api.controllers;

import br.com.gabrielferreira.votacao.api.dtos.input.AgendaInputDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Integration tests for AgendaController")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AgendaControllerIntegrationTest {

    private static final String URL = "/v1/agenda";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @SneakyThrows
    void givenValidInputWhenPostThenReturnCreated() {
        AgendaInputDTO inputDTO = AgendaInputDTO.builder()
                .title("Title")
                .description("Description")
                .build();

        String payload = objectMapper.writeValueAsString(inputDTO);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.description").value("Description"));
    }
}
