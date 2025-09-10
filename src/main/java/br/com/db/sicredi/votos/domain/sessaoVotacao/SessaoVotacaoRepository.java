package br.com.db.sicredi.votos.domain.sessaoVotacao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.db.sicredi.votos.domain.enums.StatusSessao;

public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {
    List<SessaoVotacao> findByPautasId(Long pautaId);
    List<SessaoVotacao> findByPautasIdAndStatus(Long pautaId, StatusSessao status);
}