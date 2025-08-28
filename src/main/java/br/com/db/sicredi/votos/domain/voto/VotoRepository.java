package br.com.db.sicredi.votos.domain.voto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    Optional<Voto> findBySessaoId(Long sessaoId);

    @Query("SELECT v FROM Voto v WHERE v.sessao.pauta.id = :pautaId")
    List<Voto> findByPautaId(@Param("pautaId") Long pautaId);
}
