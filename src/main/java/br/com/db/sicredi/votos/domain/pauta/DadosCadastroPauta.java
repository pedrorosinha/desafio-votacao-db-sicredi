package br.com.db.sicredi.votos.domain.pauta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosCadastroPauta(
    @NotBlank @Size(min = 5, max = 100) String titulo,
    @NotBlank @Size(min = 10, max = 500) String descricao
) {}