package br.com.db.sicredi.votos.domain.voto;

import br.com.db.sicredi.votos.domain.enums.EscolhaVoto;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroVoto(
    @NotNull Long sessaoId,
    @NotNull Long associadoId,
    @NotNull EscolhaVoto escolha
) {}
