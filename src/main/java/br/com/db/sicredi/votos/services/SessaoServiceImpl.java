package br.com.db.sicredi.votos.services;

import java.time.LocalDateTime;
import java.util.List;

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

@Service
public class SessaoServiceImpl implements SessaoService {

    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Autowired
    private PautaRepository pautaRepository;

    @Override
    public SessaoVotacao abrirSessao(DadosCadastroSessao dados) {
        List<Pauta> pautas = pautaRepository.findAllById(dados.pautaIds());
        if (pautas.size() != dados.pautaIds().size()) {
            throw new IllegalArgumentException("Uma ou mais pautas não foram encontradas");
        }

        for (Long pautaId : dados.pautaIds()) {
            List<SessaoVotacao> sessoesAbertas = sessaoVotacaoRepository.findByPautasIdAndStatus(pautaId,
                    StatusSessao.ABERTA);
            if (!sessoesAbertas.isEmpty()) {
                throw new IllegalStateException("Já existe uma sessão aberta para uma das pautas informadas.");
            }
        }

        var dataAbertura = dados.dataAbertura();
        var dataFechamento = dados.dataFechamento();

        if (dataFechamento.isBefore(dataAbertura) || dataFechamento.isEqual(dataAbertura)) {
            throw new IllegalStateException("A data de fechamento deve ser após a data de abertura.");
        }

        SessaoVotacao sessao = new SessaoVotacao(pautas, dataAbertura, dataFechamento, StatusSessao.ABERTA);

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
                        sessao.getPautas() != null && !sessao.getPautas().isEmpty() ? sessao.getPautas().get(0).getId()
                                : null,
                        sessao.getDataAbertura(),
                        sessao.getDataFechamento(),
                        calcularStatus(sessao.getDataAbertura(), sessao.getDataFechamento())));
    }

    @Override
    public SessaoVotacao buscarPorId(Long id) {
        SessaoVotacao sessao = sessaoVotacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));

        sessao.setStatus(calcularStatus(sessao.getDataAbertura(), sessao.getDataFechamento()));
        return sessao;
    }

    @Override
    public void excluirSessao(Long id) {
        SessaoVotacao sessao = sessaoVotacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));
        sessaoVotacaoRepository.delete(sessao);
    }

    private StatusSessao calcularStatus(LocalDateTime abertura, LocalDateTime fechamento) {
        LocalDateTime agora = LocalDateTime.now();
        if (agora.isBefore(abertura)) {
            return StatusSessao.FECHADA;
        } else if (agora.isAfter(fechamento)) {
            return StatusSessao.FECHADA;
        } else {
            return StatusSessao.ABERTA;
        }
    }
}
