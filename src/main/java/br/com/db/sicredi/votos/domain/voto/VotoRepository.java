package br.com.db.sicredi.votos.domain.voto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VotoRepository extends JpaRepository<Voto, Long> {
    @Query("SELECT v FROM Voto v WHERE v.pauta.id = :pautaId")
    List<Voto> findByPautaId(@Param("pautaId") Long pautaId);

    @Query("SELECT v FROM Voto v WHERE v.associado.id = :associadoId AND v.pauta.id = :pautaId")
    Optional<Voto> findByAssociadoIdAndPautaId(@Param("associadoId") Long associadoId, @Param("pautaId") Long pautaId);

    @Query("SELECT v FROM Voto v WHERE v.sessao.id = :sessaoId")
    Optional<Voto> findBySessaoId(@Param("sessaoId") Long sessaoId);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Voto v WHERE v.associado.id = :associadoId AND v.pauta.id = :pautaId")
    boolean existsByAssociadoIdAndPautaId(@Param("associadoId") Long associadoId, @Param("pautaId") Long pautaId);
}
