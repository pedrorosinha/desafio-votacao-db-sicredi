package br.com.db.sicredi.votos.domain.sessaoVotacao;

import java.time.LocalDateTime;

import br.com.db.sicredi.votos.domain.enums.StatusSessao;

public record DadosListagemSessao(
    Long id,
    Long pautaId,
    LocalDateTime dataAbertura,
    LocalDateTime dataFechamento,
    StatusSessao status
) {
    public DadosListagemSessao(Long id, Long pautaId, LocalDateTime dataAbertura, LocalDateTime dataFechamento, StatusSessao status) {
        this.id = id;
        this.pautaId = pautaId;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        this.status = status;
    }
}
