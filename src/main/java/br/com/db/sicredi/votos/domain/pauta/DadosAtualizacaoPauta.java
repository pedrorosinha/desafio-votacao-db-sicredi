package br.com.db.sicredi.votos.domain.pauta;

import jakarta.validation.constraints.NotNull;

public record DadosAtualizacaoPauta(
    @NotNull
    Long id, 
    String titulo, 
    String descricao) {}
