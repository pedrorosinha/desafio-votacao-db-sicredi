package br.com.db.sicredi.votos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.db.sicredi.votos.domain.enums.StatusSessao;
import br.com.db.sicredi.votos.domain.pauta.Pauta;
import br.com.db.sicredi.votos.domain.pauta.PautaRepository;
import br.com.db.sicredi.votos.domain.sessaoVotacao.DadosAtualizacaoSessao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.DadosCadastroSessao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.DadosListagemSessao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.SessaoVotacao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.SessaoVotacaoRepository;
import br.com.db.sicredi.votos.services.interf.SessaoService;

import java.util.List;

@Service
public class SessaoServiceImpl implements SessaoService {

    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Override
    public SessaoVotacao abrirSessao(DadosCadastroSessao dados) {
        Pauta pauta = pautaRepository.findById(dados.pautaId())
            .orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada"));

        List<SessaoVotacao> sessoesAbertas = sessaoVotacaoRepository.findByPautaIdAndStatus(pauta.getId(), StatusSessao.ABERTA);
        if (!sessoesAbertas.isEmpty()) {
            throw new IllegalStateException("Já existe uma sessão aberta para esta pauta.");
        }

        var dataAbertura = dados.dataAbertura() != null ? dados.dataAbertura() : java.time.LocalDateTime.now();
        var dataFechamento = dados.dataFechamento() != null ? dados.dataFechamento() : dataAbertura.plusMinutes(1);

        if (dataFechamento.isBefore(dataAbertura) || dataFechamento.isEqual(dataAbertura)) {
            throw new IllegalStateException("A data de fechamento deve ser após a data de abertura.");
        }

        SessaoVotacao sessao = new SessaoVotacao();
        sessao.setPauta(pauta);
        sessao.setDataAbertura(dataAbertura);
        sessao.setDataFechamento(dataFechamento);
        sessao.setStatus(StatusSessao.ABERTA);

        return sessaoVotacaoRepository.save(sessao);
    }

    @Override
    public void atualizarSessao(DadosAtualizacaoSessao dados) {
        SessaoVotacao sessao = sessaoVotacaoRepository.findById(dados.id())
            .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));
        if (dados.dataAbertura() != null) {
            sessao.setDataAbertura(dados.dataAbertura());
        }
        if (dados.dataFechamento() != null) {
            sessao.setDataFechamento(dados.dataFechamento());
        }
        if (dados.status() != null) {
            sessao.setStatus(dados.status());
        }
        sessaoVotacaoRepository.save(sessao);
    }

    @Override
    public Page<DadosListagemSessao> listarSessoes(Pageable pageable) {
        return sessaoVotacaoRepository.findAll(pageable)
            .map(sessao -> new DadosListagemSessao(
                sessao.getId(),
                sessao.getPauta() != null ? sessao.getPauta().getId() : null,
                sessao.getDataAbertura(),
                sessao.getDataFechamento(),
                sessao.getStatus()
            ));
    }

    @Override
    public SessaoVotacao buscarPorId(Long id) {
        return sessaoVotacaoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));
    }

    @Override
    public void excluirSessao(Long id) {
        SessaoVotacao sessao = sessaoVotacaoRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));
        sessaoVotacaoRepository.delete(sessao);
    }
}
