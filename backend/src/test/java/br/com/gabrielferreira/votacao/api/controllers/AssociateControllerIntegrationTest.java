package br.com.gabrielferreira.votacao.api.controllers;

import br.com.gabrielferreira.votacao.api.dtos.input.AssociateInputDTO;
import br.com.gabrielferreira.votacao.api.dtos.input.UpdateAssociateInputDTO;
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
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    private UUID associateIdInUse;

    @BeforeEach
    void setUp() {
        associateIdInUse = UUID.fromString("275cbc94-7342-4770-9c20-874b50ca3ac5");
    }

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
                .andExpect(jsonPath("$.documentNumber").value("067.380.100-44"));
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

    @Test
    @Order(10)
    @SneakyThrows
    void givenExistingAssociateWhenGetByIdThenReturnOk() {
        AssociateInputDTO inputDTO = AssociateInputDTO.builder()
                .name("João Silva")
                .email("joao@email.com")
                .password("Password@123")
                .documentNumber("12345678909")
                .build();

        MvcResult createResult = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String associateIdStr = objectMapper.readTree(responseBody).get("id").asString();
        UUID associateId = UUID.fromString(associateIdStr);

        mockMvc.perform(get(URL + "/" + associateId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(associateIdStr))
                .andExpect(jsonPath("$.name").value("João Silva"))
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.documentNumber").value("123.456.789-09"));
    }

    @Test
    @Order(11)
    @SneakyThrows
    void givenNonExistingAssociateWhenGetByIdThenReturnNotFound() {
        UUID nonExistingId = UUID.fromString("99999999-9999-9999-9999-999999999999");

        mockMvc.perform(get(URL + "/" + nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.detail").value("Associate not found with ID: " + nonExistingId));
    }

    @Test
    @Order(12)
    @SneakyThrows
    void givenValidInputWhenPutThenReturnOk() {
        AssociateInputDTO createDTO = AssociateInputDTO.builder()
                .name("João Silva")
                .email("joao@email.com")
                .password("Password@123")
                .documentNumber("98765432100")
                .build();

        MvcResult createResult = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String associateIdStr = objectMapper.readTree(responseBody).get("id").asString();
        UUID associateId = UUID.fromString(associateIdStr);

        UpdateAssociateInputDTO updateDTO = UpdateAssociateInputDTO.builder()
                .name("João Silva Updated")
                .email("joao.updated@email.com")
                .password("NewPassword@456")
                .build();

        mockMvc.perform(put(URL + "/" + associateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(associateIdStr))
                .andExpect(jsonPath("$.name").value("João Silva Updated"))
                .andExpect(jsonPath("$.email").value("joao.updated@email.com"))
                .andExpect(jsonPath("$.documentNumber").value("987.654.321-00"));
    }

    @Test
    @Order(13)
    @SneakyThrows
    void givenInvalidEmailWhenPutThenReturnBadRequest() {
        AssociateInputDTO createDTO = AssociateInputDTO.builder()
                .name("Maria Teste")
                .email("maria123@email.com")
                .password("Password@123")
                .documentNumber("89034497038")
                .build();

        MvcResult createResult = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String associateIdStr = objectMapper.readTree(responseBody).get("id").asString();
        UUID associateId = UUID.fromString(associateIdStr);

        UpdateAssociateInputDTO updateDTO = UpdateAssociateInputDTO.builder()
                .name("Maria Teste Updated")
                .email("maria.invalid.email")
                .password("NewPassword@456")
                .build();

        mockMvc.perform(put(URL + "/" + associateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Data"))
                .andExpect(jsonPath("$.fields[0].field").value("email"));
    }

    @Test
    @Order(14)
    @SneakyThrows
    void givenWeakPasswordWhenPutThenReturnBadRequest() {
        AssociateInputDTO createDTO = AssociateInputDTO.builder()
                .name("Pedro Teste")
                .email("pedro@email.com")
                .password("Password@123")
                .documentNumber("95006437073")
                .build();

        MvcResult createResult = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String associateIdStr = objectMapper.readTree(responseBody).get("id").asString();
        UUID associateId = UUID.fromString(associateIdStr);

        UpdateAssociateInputDTO updateDTO = UpdateAssociateInputDTO.builder()
                .name("Pedro Teste Updated")
                .email("pedro.updated@email.com")
                .password("weakpassword")
                .build();

        mockMvc.perform(put(URL + "/" + associateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Data"))
                .andExpect(jsonPath("$.fields[0].field").value("password"));
    }

    @Test
    @Order(15)
    @SneakyThrows
    void givenExistingEmailWhenPutThenReturnBadRequest() {
        AssociateInputDTO createDTO1 = AssociateInputDTO.builder()
                .name("Carlos Teste")
                .email("carlos@email.com")
                .password("Password@123")
                .documentNumber("16314457076")
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        AssociateInputDTO createDTO2 = AssociateInputDTO.builder()
                .name("Lucas Teste")
                .email("lucas@email.com")
                .password("Password@123")
                .documentNumber("53289565084")
                .build();

        MvcResult createResult = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO2))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String associateIdStr = objectMapper.readTree(responseBody).get("id").asString();
        UUID associateId = UUID.fromString(associateIdStr);

        UpdateAssociateInputDTO updateDTO = UpdateAssociateInputDTO.builder()
                .name("Lucas Teste Updated")
                .email("carlos@email.com")
                .password("NewPassword@456")
                .build();

        mockMvc.perform(put(URL + "/" + associateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Business Rule Violation"))
                .andExpect(jsonPath("$.detail").value("Associate with this email already exists"));
    }

    @Test
    @Order(16)
    @SneakyThrows
    void givenNonExistingAssociateWhenPutThenReturnNotFound() {
        UUID nonExistingId = UUID.fromString("99999999-9999-9999-9999-999999999999");

        UpdateAssociateInputDTO updateDTO = UpdateAssociateInputDTO.builder()
                .name("Nonexistent")
                .email("nonexistent@email.com")
                .password("Password@123")
                .build();

        mockMvc.perform(put(URL + "/" + nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.detail").value("Associate not found with ID: " + nonExistingId));
    }

    @Test
    @Order(17)
    @SneakyThrows
    void givenExistingAssociateWhenDeleteThenReturnNoContent() {
        AssociateInputDTO createDTO = AssociateInputDTO.builder()
                .name("Roberto Teste")
                .email("roberto@email.com")
                .password("Password@123")
                .documentNumber("33323672003")
                .build();

        MvcResult createResult = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String associateIdStr = objectMapper.readTree(responseBody).get("id").asString();
        UUID associateId = UUID.fromString(associateIdStr);

        mockMvc.perform(delete(URL + "/" + associateId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(URL + "/" + associateId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(18)
    @SneakyThrows
    void givenNonExistingAssociateWhenDeleteThenReturnNotFound() {
        UUID nonExistingId = UUID.fromString("99999999-9999-9999-9999-999999999999");

        mockMvc.perform(delete(URL + "/" + nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.detail").value("Associate not found with ID: " + nonExistingId));
    }

    @Test
    @Order(19)
    @SneakyThrows
    void givenAssociateInUseWhenDeleteThenReturnBadRequest() {
        mockMvc.perform(delete(URL + "/" + associateIdInUse)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Entity In Use"))
                .andExpect(jsonPath("$.detail").value("Cannot delete associate because it is in use"));
    }

    @Test
    @Order(20)
    @SneakyThrows
    void givenEmptyFilterWhenSearchThenReturnAllAssociates() {
        AssociateInputDTO createDTO = AssociateInputDTO.builder()
                .name("Ana Silva")
                .email("ana.silva@email.com")
                .password("Password@123")
                .documentNumber("11144477735")
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String filterPayload = "{}";

        mockMvc.perform(post(URL + "/search?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filterPayload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalElements").value(4))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    @Order(21)
    @SneakyThrows
    void givenNameFilterWhenSearchThenReturnFilteredAssociates() {
        AssociateInputDTO createDTO1 = AssociateInputDTO.builder()
                .name("Ana Beatriz")
                .email("ana.beatriz@email.com")
                .password("Password@123")
                .documentNumber("06738010044")
                .build();

        AssociateInputDTO createDTO2 = AssociateInputDTO.builder()
                .name("Bruno Carlos")
                .email("bruno.carlos@email.com")
                .password("Password@123")
                .documentNumber("12345678909")
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO1))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO2))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String filterPayload = "{\"name\":\"Ana\"}";

        mockMvc.perform(post(URL + "/search?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filterPayload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Ana Beatriz"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @Order(22)
    @SneakyThrows
    void givenEmailFilterWhenSearchThenReturnFilteredAssociates() {
        AssociateInputDTO createDTO = AssociateInputDTO.builder()
                .name("Carlos Eduardo")
                .email("carlos.edu@email.com")
                .password("Password@123")
                .documentNumber("98765432100")
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String filterPayload = "{\"email\":\"carlos.edu@email.com\"}";

        mockMvc.perform(post(URL + "/search?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filterPayload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].email").value("carlos.edu@email.com"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @Order(23)
    @SneakyThrows
    void givenDocumentNumberFilterWhenSearchThenReturnFilteredAssociates() {
        AssociateInputDTO createDTO = AssociateInputDTO.builder()
                .name("Diana Ferreira")
                .email("diana.ferreira@email.com")
                .password("Password@123")
                .documentNumber("48198279016")
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        String filterPayload = "{\"documentNumber\":\"48198279016\"}";

        mockMvc.perform(post(URL + "/search?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filterPayload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].documentNumber").value("481.982.790-16"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @Order(24)
    @SneakyThrows
    void givenMultipleFiltersWhenSearchThenReturnFilteredAssociates() {
        AssociateInputDTO createDTO = AssociateInputDTO.builder()
                .name("Eliana Costa")
                .email("eliana.costa@email.com")
                .password("Password@123")
                .documentNumber("90253184010")
                .build();

        MvcResult createResult = mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String associateId = objectMapper.readTree(responseBody).get("id").asString();

        String filterPayload = "{\"associateId\":\"" + associateId + "\",\"name\":\"Eliana\",\"email\":\"eliana.costa@email.com\",\"documentNumber\":\"90253184010\"}";

        mockMvc.perform(post(URL + "/search?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filterPayload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].name").value("Eliana Costa"))
                .andExpect(jsonPath("$.content[0].email").value("eliana.costa@email.com"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @Order(25)
    @SneakyThrows
    void givenNoResultsWhenSearchThenReturnEmptyPage() {
        String filterPayload = "{\"name\":\"NonexistentAssociate\"}";

        mockMvc.perform(post(URL + "/search?page=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(filterPayload)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0));
    }
}
