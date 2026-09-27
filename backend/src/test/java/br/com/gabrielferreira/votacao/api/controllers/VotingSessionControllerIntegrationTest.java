package br.com.gabrielferreira.votacao.api.controllers;

import br.com.gabrielferreira.votacao.api.dtos.input.VotingSessionInputDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
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

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Integration tests for VotingSessionController")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VotingSessionControllerIntegrationTest {

    private static final String URL = "/v1/agenda/{agendaId}/voting-sessions";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID agendaIdExistent;

    private UUID agendaIdNotExistent;

    @BeforeEach
    void setUp() {
        agendaIdExistent = UUID.fromString("d22ac18f-809f-4e1f-afcb-d6b639d9ec2d");
        agendaIdNotExistent = UUID.randomUUID();
    }

    @Test
    @Order(1)
    @SneakyThrows
    void givenValidInputWhenPostThenReturnCreated() {
        VotingSessionInputDTO dto = VotingSessionInputDTO.builder()
                .votingStartTime(OffsetDateTime.now().plusHours(1))
                .votingEndTime(OffsetDateTime.now().plusHours(2))
                .build();

        String payload = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(URL, agendaIdExistent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.votingStartTime", notNullValue()))
                .andExpect(jsonPath("$.votingEndTime", notNullValue()))
               .andExpect(jsonPath("$.votingStatus").value("OPEN"));
    }

    @Test
    @Order(2)
    @SneakyThrows
    void givenAgendaNotExistsWhenPostThenReturnNotFound() {
        VotingSessionInputDTO dto = VotingSessionInputDTO.builder()
                .votingStartTime(OffsetDateTime.now().plusHours(1))
                .votingEndTime(OffsetDateTime.now().plusHours(2))
                .build();

        String payload = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post(URL, agendaIdNotExistent)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
