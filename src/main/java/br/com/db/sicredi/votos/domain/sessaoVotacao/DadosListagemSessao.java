package br.com.db.sicredi.votos.domain.sessaoVotacao;

import java.time.LocalDateTime;

import br.com.db.sicredi.votos.domain.enums.StatusSessao;

public record DadosListagemSessao(
    Long id,
    Long pautaId,
    LocalDateTime dataAbertura,
    LocalDateTime dataFechamento,
    StatusSessao status
) {}
