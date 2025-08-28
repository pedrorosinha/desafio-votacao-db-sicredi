package br.com.db.sicredi.votos.services.interf;

import br.com.db.sicredi.votos.domain.voto.DadosCadastroVoto;
import br.com.db.sicredi.votos.domain.voto.ResultadoVotacao;

public interface VotoService {
    void registrarVoto(DadosCadastroVoto dados);
    ResultadoVotacao contabilizarVotos(Long pautaId);
}
