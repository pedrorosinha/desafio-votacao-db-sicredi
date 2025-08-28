package br.com.db.sicredi.votos.services;

import br.com.db.sicredi.votos.domain.enums.EscolhaVoto;
import br.com.db.sicredi.votos.domain.enums.StatusSessao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.SessaoVotacao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.SessaoVotacaoRepository;
import br.com.db.sicredi.votos.domain.voto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoServiceImplTest {

    @Mock VotoRepository votoRepository;
    @Mock SessaoVotacaoRepository sessaoVotacaoRepository;
    @InjectMocks VotoServiceImpl votoService;

    @Test
    @DisplayName("Deve registrar voto com sucesso")
    void registrarVoto() {
        var sessao = new SessaoVotacao();
        sessao.setId(1L);
        sessao.setStatus(StatusSessao.ABERTA);
        sessao.setDataAbertura(LocalDateTime.now().minusMinutes(1));
        sessao.setDataFechamento(LocalDateTime.now().plusMinutes(1));
        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessao));

        var dados = new DadosCadastroVoto(1L, EscolhaVoto.SIM);
        assertDoesNotThrow(() -> votoService.registrarVoto(dados));
        verify(votoRepository).save(any(Voto.class));
    }

    @Test
    @DisplayName("Não deve votar em sessão inexistente")
    void votarSessaoInexistente() {
        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.empty());
        var dados = new DadosCadastroVoto(1L, EscolhaVoto.SIM);
        Exception ex = assertThrows(IllegalArgumentException.class, () -> votoService.registrarVoto(dados));
        assertEquals("Sessão não encontrada", ex.getMessage());
    }

    @Test
    @DisplayName("Não deve votar em sessão fechada")
    void votarSessaoFechada() {
        var sessao = new SessaoVotacao();
        sessao.setId(1L);
        sessao.setStatus(StatusSessao.FECHADA);
        sessao.setDataAbertura(LocalDateTime.now().minusMinutes(2));
        sessao.setDataFechamento(LocalDateTime.now().minusMinutes(1));
        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        var dados = new DadosCadastroVoto(1L, EscolhaVoto.SIM);
        Exception ex = assertThrows(IllegalStateException.class, () -> votoService.registrarVoto(dados));
        assertEquals("Sessão de votação não está aberta.", ex.getMessage());
    }

    @Test
    @DisplayName("Não deve votar antes da data de abertura")
    void votarAntesDaDataAbertura() {
        var sessao = new SessaoVotacao();
        sessao.setId(1L);
        sessao.setStatus(StatusSessao.ABERTA);
        sessao.setDataAbertura(LocalDateTime.now().plusMinutes(2));
        sessao.setDataFechamento(LocalDateTime.now().plusMinutes(10));
        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        var dados = new DadosCadastroVoto(1L, EscolhaVoto.SIM);
        Exception ex = assertThrows(IllegalStateException.class, () -> votoService.registrarVoto(dados));
        assertEquals("Sessão de votação não está aberta.", ex.getMessage());
    }

    @Test
    @DisplayName("Não deve votar após a data de fechamento")
    void votarDepoisDaDataFechamento() {
        var sessao = new SessaoVotacao();
        sessao.setId(1L);
        sessao.setStatus(StatusSessao.ABERTA);
        sessao.setDataAbertura(LocalDateTime.now().minusMinutes(10));
        sessao.setDataFechamento(LocalDateTime.now().minusMinutes(5));
        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        var dados = new DadosCadastroVoto(1L, EscolhaVoto.SIM);
        Exception ex = assertThrows(IllegalStateException.class, () -> votoService.registrarVoto(dados));
        assertEquals("Sessão de votação não está aberta.", ex.getMessage());
    }

    @Test
    @DisplayName("Deve contabilizar votos corretamente")
    void contabilizarVotos() {
        var voto1 = new Voto();
        voto1.setEscolha(EscolhaVoto.SIM);
        var voto2 = new Voto();
        voto2.setEscolha(EscolhaVoto.NAO);
        var voto3 = new Voto();
        voto3.setEscolha(EscolhaVoto.SIM);
        when(votoRepository.findByPautaId(1L)).thenReturn(List.of(voto1, voto2, voto3));

        var resultado = votoService.contabilizarVotos(1L);

        assertEquals(3, resultado.totalVotos());
        assertEquals(2, resultado.votosSim());
        assertEquals(1, resultado.votosNao());
        assertEquals(StatusResultadoVotos.APROVADA, resultado.resultado());
    }

    @Test
    @DisplayName("Deve retornar resultado REPROVADA quando votosNao > votosSim")
    void contabilizarVotosReprovada() {
        var voto1 = new Voto(); voto1.setEscolha(EscolhaVoto.NAO);
        var voto2 = new Voto(); voto2.setEscolha(EscolhaVoto.NAO);
        var voto3 = new Voto(); voto3.setEscolha(EscolhaVoto.SIM);
        when(votoRepository.findByPautaId(1L)).thenReturn(List.of(voto1, voto2, voto3));

        var resultado = votoService.contabilizarVotos(1L);

        assertEquals(StatusResultadoVotos.REPROVADA, resultado.resultado());
    }

    @Test
    @DisplayName("Deve retornar resultado EMPATE quando votosSim == votosNao")
    void contabilizarVotosEmpate() {
        var voto1 = new Voto(); voto1.setEscolha(EscolhaVoto.SIM);
        var voto2 = new Voto(); voto2.setEscolha(EscolhaVoto.NAO);
        when(votoRepository.findByPautaId(1L)).thenReturn(List.of(voto1, voto2));

        var resultado = votoService.contabilizarVotos(1L);

        assertEquals(StatusResultadoVotos.EMPATE, resultado.resultado());
    }

    @Test
    @DisplayName("Não deve permitir voto duplo na mesma sessão")
    void naoPermitirVotoDuplo() {
        when(votoRepository.findBySessaoId(1L)).thenReturn(Optional.of(new Voto()));

        var dados1 = new DadosCadastroVoto(1L, EscolhaVoto.SIM);

        Exception ex = assertThrows(IllegalStateException.class, () -> votoService.registrarVoto(dados1));
        assertEquals("Já votou nesta sessão.", ex.getMessage());
        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    @DisplayName("Deve contabilizar votos apenas da pauta correta")
    void contabilizarVotosPorPautaCorreta() {
        var voto1 = new Voto();
        voto1.setEscolha(EscolhaVoto.SIM);
        var voto2 = new Voto();
        voto2.setEscolha(EscolhaVoto.NAO);

        when(votoRepository.findByPautaId(99L)).thenReturn(List.of());

        var resultado = votoService.contabilizarVotos(99L);

        assertEquals(0, resultado.totalVotos());
        assertEquals(0, resultado.votosSim());
        assertEquals(0, resultado.votosNao());
    }
}
