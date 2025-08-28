package br.com.db.sicredi.votos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.db.sicredi.votos.domain.enums.EscolhaVoto;
import br.com.db.sicredi.votos.domain.enums.StatusSessao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.SessaoVotacao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.SessaoVotacaoRepository;
import br.com.db.sicredi.votos.domain.voto.DadosCadastroVoto;
import br.com.db.sicredi.votos.domain.voto.ResultadoVotacao;
import br.com.db.sicredi.votos.domain.voto.StatusResultadoVotos;
import br.com.db.sicredi.votos.domain.voto.Voto;
import br.com.db.sicredi.votos.domain.voto.VotoRepository;
import br.com.db.sicredi.votos.services.interf.VotoService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VotoServiceImpl implements VotoService {

    @Autowired
    private VotoRepository votoRepository;

    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;

    @Override
    public void registrarVoto(DadosCadastroVoto dados) {
        if (votoRepository.findBySessaoId(dados.sessaoId()).isPresent()) {
            throw new IllegalStateException("Já votou nesta sessão.");
        }

        SessaoVotacao sessao = sessaoVotacaoRepository.findById(dados.sessaoId())
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada"));
        LocalDateTime agora = LocalDateTime.now();
        if (sessao.getStatus() != StatusSessao.ABERTA ||
                agora.isBefore(sessao.getDataAbertura()) ||
                agora.isAfter(sessao.getDataFechamento())) {
            throw new IllegalStateException("Sessão de votação não está aberta.");
        }

        Voto voto = new Voto();
        voto.setId(null);
        voto.setSessao(sessao);
        voto.setEscolha(dados.escolha());
        voto.setDataCriacao(agora);

        votoRepository.save(voto);
    }

    @Override
    public ResultadoVotacao contabilizarVotos(Long pautaId) {
        List<Voto> votos = votoRepository.findByPautaId(pautaId);
        int votosSim = (int) votos.stream().filter(v -> v.getEscolha() == EscolhaVoto.SIM).count();
        int votosNao = (int) votos.stream().filter(v -> v.getEscolha() == EscolhaVoto.NAO).count();
        int total = votosSim + votosNao;

        StatusResultadoVotos status;
        if (votosSim > votosNao) {
            status = StatusResultadoVotos.APROVADA;
        } else if (votosNao > votosSim) {
            status = StatusResultadoVotos.REPROVADA;
        } else {
            status = StatusResultadoVotos.EMPATE;
        }

        return new ResultadoVotacao(pautaId, total, votosSim, votosNao, status);
    }
}
