package br.com.gabrielferreira.votacao.domain.specs;

import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import br.com.gabrielferreira.votacao.domain.repositories.filter.associate.AssociateFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AssociateSpec implements Specification<AssociateEntity> {

    @Serial
    private static final long serialVersionUID = 3480545389646643043L;

    private final AssociateFilter associateFilter;

    @Override
    public Predicate toPredicate(@NonNull Root<AssociateEntity> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (associateFilter.hasAssociateId()) {
            Predicate predicateAssociateId = criteriaBuilder.equal(root.get("idExternalUuid"), associateFilter.associateId());
            predicates.add(predicateAssociateId);
        }

        if (associateFilter.hasName()) {
            Predicate predicateName = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")), "%" + associateFilter.name().toLowerCase() + "%"
            );
            predicates.add(predicateName);
        }

        if (associateFilter.hasEmail()) {
            Predicate predicateEmail = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("email")), "%" + associateFilter.email().toLowerCase() + "%"
            );
            predicates.add(predicateEmail);
        }

        if (associateFilter.hasDocumentNumber()) {
            Predicate predicateDocumentNumber = criteriaBuilder.equal(
                    root.get("documentNumber"), associateFilter.documentNumber()
            );
            predicates.add(predicateDocumentNumber);
        }

        if (predicates.isEmpty()) {
            return criteriaBuilder.conjunction();
        }

        return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
    }
}
