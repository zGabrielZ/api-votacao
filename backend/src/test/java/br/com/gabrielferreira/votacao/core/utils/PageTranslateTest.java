package br.com.gabrielferreira.votacao.core.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Unit tests for PageTranslate")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PageTranslateTest {

    @Test
    @Order(1)
    void givenPageableWithAssociateIdSortWhenToPageableThenReturnTranslatedPageable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("associateId").ascending());
        Map<String, String> fieldsMapping = PageTranslate.getAssociatePageableFieldsMapping();

        Pageable result = PageTranslate.toPageable(pageable, fieldsMapping);

        assertNotNull(result);
        assertEquals(0, result.getPageNumber());
        assertEquals(10, result.getPageSize());
        assertEquals("idExternalUuid", result.getSort().stream().findFirst().get().getProperty());
        assertEquals(Sort.Direction.ASC, result.getSort().stream().findFirst().get().getDirection());
    }

    @Test
    @Order(2)
    void givenPageableWithNameSortWhenToPageableThenReturnTranslatedPageable() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("name").descending());
        Map<String, String> fieldsMapping = PageTranslate.getAssociatePageableFieldsMapping();

        Pageable result = PageTranslate.toPageable(pageable, fieldsMapping);

        assertNotNull(result);
        assertEquals(0, result.getPageNumber());
        assertEquals(5, result.getPageSize());
        assertEquals("name", result.getSort().stream().findFirst().get().getProperty());
        assertEquals(Sort.Direction.DESC, result.getSort().stream().findFirst().get().getDirection());
    }

    @Test
    @Order(3)
    void givenPageableWithEmailSortWhenToPageableThenReturnTranslatedPageable() {
        Pageable pageable = PageRequest.of(1, 20, Sort.by("email").ascending());
        Map<String, String> fieldsMapping = PageTranslate.getAssociatePageableFieldsMapping();

        Pageable result = PageTranslate.toPageable(pageable, fieldsMapping);

        assertNotNull(result);
        assertEquals(1, result.getPageNumber());
        assertEquals(20, result.getPageSize());
        assertEquals("email", result.getSort().stream().findFirst().get().getProperty());
    }

    @Test
    @Order(4)
    void givenPageableWithUnmappedFieldWhenToPageableThenReturnPageableWithoutSort() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("unknownField").ascending());
        Map<String, String> fieldsMapping = PageTranslate.getAssociatePageableFieldsMapping();

        Pageable result = PageTranslate.toPageable(pageable, fieldsMapping);

        assertNotNull(result);
        assertEquals(0, result.getPageNumber());
        assertEquals(10, result.getPageSize());
    }

    @Test
    @Order(5)
    void givenGetAssociatePageableFieldsMappingWhenCalledThenReturnMapping() {
        Map<String, String> mapping = PageTranslate.getAssociatePageableFieldsMapping();

        assertNotNull(mapping);
        assertEquals("idExternalUuid", mapping.get("associateId"));
        assertEquals("name", mapping.get("name"));
        assertEquals("email", mapping.get("email"));
        assertEquals("documentNumber", mapping.get("documentNumber"));
    }
}
