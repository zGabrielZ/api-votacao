package br.com.gabrielferreira.votacao.api.controllers;

import br.com.gabrielferreira.votacao.api.dtos.input.AssociateInputDTO;
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

@DisplayName("Integration tests for AssociateController")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AssociateControllerIntegrationTest {

    private static final String URL = "/v1/associates";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @SneakyThrows
    void givenValidInputWhenPostThenReturnCreated() {
        AssociateInputDTO inputDTO = AssociateInputDTO.builder()
                .name("Ana Costa")
                .email("ana.costa@email.com")
                .password("Password@123")
                .documentNumber("06738010044")
                .build();

        String payload = objectMapper.writeValueAsString(inputDTO);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name").value("Ana Costa"))
                .andExpect(jsonPath("$.email").value("ana.costa@email.com"))
                .andExpect(jsonPath("$.documentNumber").value("06738010044"));
    }

    @Test
    @Order(2)
    @SneakyThrows
    void givenNameLengthOutOfRangeWhenPostThenReturnBadRequest() {
        AssociateInputDTO inputDTO = AssociateInputDTO.builder()
                .name("A".repeat(256))
                .email("valid@email.com")
                .password("Password@123")
                .documentNumber("06738010044")
                .build();

        String payload = objectMapper.writeValueAsString(inputDTO);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Data"))
                .andExpect(jsonPath("$.fields[0].field").value("name"));
    }

    @Test
    @Order(3)
    @SneakyThrows
    void givenInvalidEmailWhenPostThenReturnBadRequest() {
        AssociateInputDTO inputDTO = AssociateInputDTO.builder()
                .name("Maria Souza")
                .email("maria.email.com")
                .password("Password@123")
                .documentNumber("06738010044")
                .build();

        String payload = objectMapper.writeValueAsString(inputDTO);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Data"))
                .andExpect(jsonPath("$.fields[0].field").value("email"));
    }

    @Test
    @Order(4)
    @SneakyThrows
    void givenPasswordLengthOutOfRangeWhenPostThenReturnBadRequest() {
        AssociateInputDTO inputDTO = AssociateInputDTO.builder()
                .name("Maria Souza")
                .email("maria@email.com")
                .password("A".repeat(256))
                .documentNumber("06738010044")
                .build();

        String payload = objectMapper.writeValueAsString(inputDTO);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Data"))
                .andExpect(jsonPath("$.fields[0].field").value("password"));
    }

    @Test
    @Order(5)
    @SneakyThrows
    void givenWeakPasswordWhenPostThenReturnBadRequest() {
        AssociateInputDTO inputDTO = AssociateInputDTO.builder()
                .name("Maria Souza")
                .email("maria@email.com")
                .password("password123")
                .documentNumber("06738010044")
                .build();

        String payload = objectMapper.writeValueAsString(inputDTO);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Data"))
                .andExpect(jsonPath("$.fields[0].field").value("password"));
    }

    @Test
    @Order(6)
    @SneakyThrows
    void givenInvalidDocumentNumberWhenPostThenReturnBadRequest() {
        AssociateInputDTO inputDTO = AssociateInputDTO.builder()
                .name("Maria Souza")
                .email("maria@email.com")
                .password("Password@123")
                .documentNumber("123.456.789-00")
                .build();

        String payload = objectMapper.writeValueAsString(inputDTO);

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Data"))
                .andExpect(jsonPath("$.fields[0].field").value("documentNumber"));
    }

    @Test
    @Order(7)
    @SneakyThrows
    void givenMissingRequiredFieldsWhenPostThenReturnBadRequest() {
        String payload = "{\"name\":\"\",\"email\":\"\",\"password\":\"\",\"documentNumber\":\"\"}";

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Data"));
    }

    @Test
    @Order(8)
    @SneakyThrows
    void givenExistingEmailWhenPostThenReturnBadRequest() {
        AssociateInputDTO firstPayload = AssociateInputDTO.builder()
                .name("Ana Costa")
                .email("duplicate@email.com")
                .password("Password@123")
                .documentNumber("06738010044")
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstPayload))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        AssociateInputDTO duplicatePayload = AssociateInputDTO.builder()
                .name("Beatriz Rocha")
                .email("duplicate@email.com")
                .password("Password@123")
                .documentNumber("12345678909")
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicatePayload))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Business Rule Violation"))
                .andExpect(jsonPath("$.detail").value("Associate with this email already exists"));
    }

    @Test
    @Order(9)
    @SneakyThrows
    void givenExistingDocumentNumberWhenPostThenReturnBadRequest() {
        AssociateInputDTO firstPayload = AssociateInputDTO.builder()
                .name("Ana Costa")
                .email("document@email.com")
                .password("Password@123")
                .documentNumber("11144477735")
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstPayload))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        AssociateInputDTO duplicatePayload = AssociateInputDTO.builder()
                .name("Beatriz Rocha")
                .email("another@email.com")
                .password("Password@123")
                .documentNumber("11144477735")
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicatePayload))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Business Rule Violation"))
                .andExpect(jsonPath("$.detail").value("Associate with this document number already exists"));
    }
}
