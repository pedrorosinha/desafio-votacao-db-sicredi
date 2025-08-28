package br.com.db.sicredi.votos.domain.voto;

public record ResultadoVotacao(
    Long pautaId,
    int totalVotos,
    int votosSim,
    int votosNao,
    StatusResultadoVotos resultado
) {}
