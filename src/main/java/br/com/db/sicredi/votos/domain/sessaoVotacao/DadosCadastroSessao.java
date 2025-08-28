package br.com.db.sicredi.votos.domain.sessaoVotacao;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroSessao(
    @NotNull Long pautaId,
    @NotNull LocalDateTime dataAbertura,
    @NotNull @Future LocalDateTime dataFechamento
) {}