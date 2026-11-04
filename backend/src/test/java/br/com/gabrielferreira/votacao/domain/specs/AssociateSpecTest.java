package br.com.gabrielferreira.votacao.domain.specs;

import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import br.com.gabrielferreira.votacao.domain.repositories.filter.associate.AssociateFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("Unit tests for AssociateSpec")
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AssociateSpecTest {

    @Mock
    private Root<AssociateEntity> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Captor
    private ArgumentCaptor<Predicate[]> predicatesCaptor;

    private Predicate mockPredicate;

    @BeforeEach
    void setUp() {
        mockPredicate = mock(Predicate.class);
    }

    @Test
    @Order(1)
    void givenEmptyFilterWhenToPredicateThenReturnConjunction() {
        AssociateFilter filter = AssociateFilter.builder().build();
        AssociateSpec spec = new AssociateSpec(filter);

        when(criteriaBuilder.conjunction()).thenReturn(mockPredicate);

        Predicate result = spec.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).conjunction();
    }

    @Test
    @Order(2)
    void givenAssociateIdFilterWhenToPredicateThenCreateEqualPredicate() {
        UUID associateId = UUID.randomUUID();
        AssociateFilter filter = AssociateFilter.builder()
                .associateId(associateId)
                .build();
        AssociateSpec spec = new AssociateSpec(filter);

        @SuppressWarnings("unchecked")
        Path<UUID> path = mock(Path.class);
        doReturn(path).when(root).get("idExternalUuid");
        when(criteriaBuilder.equal(path, associateId)).thenReturn(mockPredicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(mockPredicate);

        Predicate result = spec.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(root).get("idExternalUuid");
        verify(criteriaBuilder).equal(path, associateId);
    }

    @Test
    @Order(3)
    void givenNameFilterWhenToPredicateThenCreateLikePredicate() {
        AssociateFilter filter = AssociateFilter.builder()
                .name("John")
                .build();
        AssociateSpec spec = new AssociateSpec(filter);

        @SuppressWarnings("unchecked")
        Path<String> path = mock(Path.class);
        @SuppressWarnings("unchecked")
        Expression<String> lowerExpression = mock(Expression.class);

        doReturn(path).when(root).get("name");
        when(criteriaBuilder.lower(path)).thenReturn(lowerExpression);
        when(criteriaBuilder.like(lowerExpression, "%john%")).thenReturn(mockPredicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(mockPredicate);

        Predicate result = spec.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(root).get("name");
        verify(criteriaBuilder).lower(path);
        verify(criteriaBuilder).like(lowerExpression, "%john%");
    }

    @Test
    @Order(4)
    void givenEmailFilterWhenToPredicateThenCreateLikePredicate() {
        AssociateFilter filter = AssociateFilter.builder()
                .email("john@email.com")
                .build();
        AssociateSpec spec = new AssociateSpec(filter);

        @SuppressWarnings("unchecked")
        Path<String> path = mock(Path.class);
        @SuppressWarnings("unchecked")
        Expression<String> lowerExpression = mock(Expression.class);

        doReturn(path).when(root).get("email");
        when(criteriaBuilder.lower(path)).thenReturn(lowerExpression);
        when(criteriaBuilder.like(lowerExpression, "%john@email.com%")).thenReturn(mockPredicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(mockPredicate);

        Predicate result = spec.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(root).get("email");
        verify(criteriaBuilder).lower(path);
        verify(criteriaBuilder).like(lowerExpression, "%john@email.com%");
    }

    @Test
    @Order(5)
    void givenDocumentNumberFilterWhenToPredicateThenCreateEqualPredicate() {
        AssociateFilter filter = AssociateFilter.builder()
                .documentNumber("12345678900")
                .build();
        AssociateSpec spec = new AssociateSpec(filter);

        @SuppressWarnings("unchecked")
        Path<String> path = mock(Path.class);
        doReturn(path).when(root).get("documentNumber");
        when(criteriaBuilder.equal(path, "12345678900")).thenReturn(mockPredicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(mockPredicate);

        Predicate result = spec.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(root).get("documentNumber");
        verify(criteriaBuilder).equal(path, "12345678900");
    }

    @Test
    @Order(6)
    void givenMultipleFiltersWhenToPredicateThenCombineWithAnd() {
        UUID associateId = UUID.randomUUID();
        AssociateFilter filter = AssociateFilter.builder()
                .associateId(associateId)
                .name("John")
                .email("john@email.com")
                .documentNumber("12345678900")
                .build();
        AssociateSpec spec = new AssociateSpec(filter);

        @SuppressWarnings("unchecked")
        Path<UUID> associateIdPath = mock(Path.class);
        @SuppressWarnings("unchecked")
        Path<String> namePath = mock(Path.class);
        @SuppressWarnings("unchecked")
        Path<String> emailPath = mock(Path.class);
        @SuppressWarnings("unchecked")
        Path<String> documentNumberPath = mock(Path.class);
        @SuppressWarnings("unchecked")
        Expression<String> lowerNameExpression = mock(Expression.class);
        @SuppressWarnings("unchecked")
        Expression<String> lowerEmailExpression = mock(Expression.class);

        doReturn(associateIdPath).when(root).get("idExternalUuid");
        doReturn(namePath).when(root).get("name");
        doReturn(emailPath).when(root).get("email");
        doReturn(documentNumberPath).when(root).get("documentNumber");

        when(criteriaBuilder.equal(associateIdPath, associateId)).thenReturn(mockPredicate);
        when(criteriaBuilder.lower(namePath)).thenReturn(lowerNameExpression);
        when(criteriaBuilder.like(lowerNameExpression, "%john%")).thenReturn(mockPredicate);
        when(criteriaBuilder.lower(emailPath)).thenReturn(lowerEmailExpression);
        when(criteriaBuilder.like(lowerEmailExpression, "%john@email.com%")).thenReturn(mockPredicate);
        when(criteriaBuilder.equal(documentNumberPath, "12345678900")).thenReturn(mockPredicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(mockPredicate);

        Predicate result = spec.toPredicate(root, query, criteriaBuilder);

        assertNotNull(result);
        verify(criteriaBuilder).and(predicatesCaptor.capture());
        Predicate[] predicates = predicatesCaptor.getValue();
        assertEquals(4, predicates.length);
    }

    @Test
    @Order(7)
    void givenNameFilterWhenToPredicateThenLowercaseFilter() {
        AssociateFilter filter = AssociateFilter.builder()
                .name("john")
                .build();
        AssociateSpec spec = new AssociateSpec(filter);

        @SuppressWarnings("unchecked")
        Path<String> path = mock(Path.class);
        @SuppressWarnings("unchecked")
        Expression<String> lowerExpression = mock(Expression.class);

        doReturn(path).when(root).get("name");
        when(criteriaBuilder.lower(path)).thenReturn(lowerExpression);
        when(criteriaBuilder.like(lowerExpression, "%john%")).thenReturn(mockPredicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(mockPredicate);

        spec.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).like(lowerExpression, "%john%");
    }

    @Test
    @Order(8)
    void givenEmailFilterWhenToPredicateThenLowercaseFilter() {
        AssociateFilter filter = AssociateFilter.builder()
                .email("john@email.com")
                .build();
        AssociateSpec spec = new AssociateSpec(filter);

        @SuppressWarnings("unchecked")
        Path<String> path = mock(Path.class);
        @SuppressWarnings("unchecked")
        Expression<String> lowerExpression = mock(Expression.class);

        doReturn(path).when(root).get("email");
        when(criteriaBuilder.lower(path)).thenReturn(lowerExpression);
        when(criteriaBuilder.like(lowerExpression, "%john@email.com%")).thenReturn(mockPredicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(mockPredicate);

        spec.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).like(lowerExpression, "%john@email.com%");
    }
}
