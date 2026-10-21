package br.com.gabrielferreira.votacao.domain.repositories;

import br.com.gabrielferreira.votacao.domain.entities.AssociateEntity;
import br.com.gabrielferreira.votacao.domain.repositories.projection.AssociateDuplicateCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssociateRepository extends JpaRepository<AssociateEntity, Long> {

    Optional<AssociateEntity> findByIdExternalUuid(UUID externalUuid);

    @Query(value = "SELECT COUNT(a.id) AS totalMatches FROM TB_ASSOCIATE a WHERE a.DOCUMENT_NUMBER = :documentNumber", nativeQuery = true)
    AssociateDuplicateCheck existsDocumentNumber(@Param("documentNumber") String documentNumber);

    @Query(value = "SELECT COUNT(a.id) AS totalMatches FROM TB_ASSOCIATE a WHERE a.EMAIL = :email", nativeQuery = true)
    AssociateDuplicateCheck existsEmail(@Param("email") String email);
}
