package br.com.db.sicredi.votos.domain.voto;

import br.com.db.sicredi.votos.domain.enums.EscolhaVoto;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroVoto(Long sessaoId, @NotNull EscolhaVoto escolha) {
}
