package br.com.db.sicredi.votos.domain.sessaoVotacao;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroSessao(
    @NotEmpty List<Long> pautaIds,
    @NotNull LocalDateTime dataAbertura,
    @NotNull @Future LocalDateTime dataFechamento
) {}