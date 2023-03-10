package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.Cozinha;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CozinhaRepository extends CustomJPARepository<Cozinha, Long> {
    List<Cozinha> findTodasByNomeContaining(String nome); // containing -> like query
    Optional<Cozinha> findByNome(String nome);
    boolean existsByNome(String nome);
}
