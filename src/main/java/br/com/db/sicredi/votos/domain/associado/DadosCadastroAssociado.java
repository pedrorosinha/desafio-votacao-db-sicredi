package br.com.db.sicredi.votos.domain.associado;

import jakarta.validation.constraints.NotNull;

public record DadosCadastroAssociado(
        @NotNull
        String nome
) {
}
