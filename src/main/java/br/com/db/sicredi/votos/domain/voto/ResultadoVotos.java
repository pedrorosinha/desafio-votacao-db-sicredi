package br.com.db.sicredi.votos.domain.voto;

public record ResultadoVotos(
    Long pautaId,
    int totalVotos,
    int votosSim,
    int votosNao,
    StatusResultadoVotos status
) {}

