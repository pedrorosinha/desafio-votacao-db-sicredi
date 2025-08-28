package br.com.db.sicredi.votos.services.interf;

import br.com.db.sicredi.votos.domain.sessaoVotacao.DadosAtualizacaoSessao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.DadosCadastroSessao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.DadosListagemSessao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.SessaoVotacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SessaoService {
    SessaoVotacao abrirSessao(DadosCadastroSessao dados);
    void atualizarSessao(DadosAtualizacaoSessao dados);
    Page<DadosListagemSessao> listarSessoes(Pageable pageable);
    SessaoVotacao buscarPorId(Long id);
    void excluirSessao(Long id);
}
