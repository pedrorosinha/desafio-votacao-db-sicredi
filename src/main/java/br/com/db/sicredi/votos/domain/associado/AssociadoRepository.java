package br.com.db.sicredi.votos.domain.associado;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssociadoRepository extends JpaRepository<Associado, Long> {

    Optional<Associado> findByNome(String nome);
}
