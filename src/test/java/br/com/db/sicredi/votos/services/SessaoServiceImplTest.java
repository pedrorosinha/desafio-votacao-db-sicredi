package br.com.db.sicredi.votos.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.db.sicredi.votos.domain.enums.StatusSessao;
import br.com.db.sicredi.votos.domain.pauta.Pauta;
import br.com.db.sicredi.votos.domain.pauta.PautaRepository;
import br.com.db.sicredi.votos.domain.sessaoVotacao.DadosAtualizacaoSessao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.DadosCadastroSessao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.DadosListagemSessao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.SessaoVotacao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.SessaoVotacaoRepository;

@ExtendWith(MockitoExtension.class)
class SessaoServiceImplTest {

    @Mock
    SessaoVotacaoRepository sessaoVotacaoRepository;
    @Mock
    PautaRepository pautaRepository;
    @InjectMocks
    SessaoServiceImpl sessaoService;

    @Test
    @DisplayName("Deve abrir uma sessão de votação com sucesso")
    void abrirSessao() {
        var pauta = new Pauta();
        pauta.setId(1L);
        var dados = new DadosCadastroSessao(java.util.List.of(1L), LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1));
        when(pautaRepository.findAllById(java.util.List.of(1L))).thenReturn(java.util.List.of(pauta));
        when(sessaoVotacaoRepository.findByPautasIdAndStatus(1L, StatusSessao.ABERTA))
                .thenReturn(Collections.emptyList());
        when(sessaoVotacaoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        SessaoVotacao sessao = sessaoService.abrirSessao(dados);

        assertNotNull(sessao);
        assertEquals(pauta, sessao.getPautas().get(0));
        assertEquals(StatusSessao.ABERTA, sessao.getStatus());
        verify(sessaoVotacaoRepository).save(any());
    }

    @Test
    @DisplayName("Não deve abrir sessão se pauta não existir")
    void abrirSessaoPautaInexistente() {
        var dados = new DadosCadastroSessao(java.util.List.of(99L), LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1));
        when(pautaRepository.findAllById(java.util.List.of(99L))).thenReturn(java.util.List.of());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> sessaoService.abrirSessao(dados));
        assertEquals("Uma ou mais pautas não foram encontradas", ex.getMessage());
    }

    @Test
    @DisplayName("Não deve abrir sessão se já houver sessão aberta")
    void abrirSessaoJaExisteAberta() {
        var pauta = new Pauta();
        pauta.setId(1L);
        var dados = new DadosCadastroSessao(java.util.List.of(1L), LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1));
        when(pautaRepository.findAllById(java.util.List.of(1L))).thenReturn(java.util.List.of(pauta));
        when(sessaoVotacaoRepository.findByPautasIdAndStatus(1L, StatusSessao.ABERTA))
                .thenReturn(Arrays.asList(new SessaoVotacao()));

        Exception ex = assertThrows(IllegalStateException.class, () -> sessaoService.abrirSessao(dados));
        assertEquals("Já existe uma sessão aberta para uma das pautas informadas.", ex.getMessage());
    }

    @Test
    @DisplayName("Não deve abrir sessão se data de fechamento for antes ou igual à abertura")
    void abrirSessaoDataFechamentoAntesOuIgualAbertura() {
        var pauta = new Pauta();
        pauta.setId(1L);
        var abertura = LocalDateTime.now().plusMinutes(10);
        var fechamento = abertura.minusMinutes(1);
        var dados = new DadosCadastroSessao(java.util.List.of(1L), abertura, fechamento);
        when(pautaRepository.findAllById(java.util.List.of(1L))).thenReturn(java.util.List.of(pauta));
        when(sessaoVotacaoRepository.findByPautasIdAndStatus(1L, StatusSessao.ABERTA))
                .thenReturn(Collections.emptyList());

        Exception ex = assertThrows(IllegalStateException.class, () -> sessaoService.abrirSessao(dados));
        assertEquals("A data de fechamento deve ser após a data de abertura.", ex.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar uma sessão existente")
    void atualizarSessao() {
        var sessao = new SessaoVotacao();
        sessao.setId(1L);
        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        var novaData = LocalDateTime.now().plusMinutes(10);
        var dados = new DadosAtualizacaoSessao(1L, 1L, novaData, novaData.plusMinutes(1), StatusSessao.FECHADA);

        sessaoService.atualizarSessao(dados);

        assertEquals(novaData, sessao.getDataAbertura());
        assertEquals(novaData.plusMinutes(1), sessao.getDataFechamento());
        assertEquals(StatusSessao.FECHADA, sessao.getStatus());
        verify(sessaoVotacaoRepository).save(sessao);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar sessão inexistente")
    void atualizarSessaoInexistente() {
        var dados = new DadosAtualizacaoSessao(99L, 1L, LocalDateTime.now(), LocalDateTime.now().plusMinutes(1),
                StatusSessao.ABERTA);
        when(sessaoVotacaoRepository.findById(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> sessaoService.atualizarSessao(dados));
        assertEquals("Sessão não encontrada", ex.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar sessão sem alterar campos se DTO vazio")
    void atualizarSessaoSemAlterarCampos() {
        var sessao = new SessaoVotacao();
        sessao.setId(1L);
        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        var dados = new DadosAtualizacaoSessao(1L, null, null, null, null);

        sessaoService.atualizarSessao(dados);

        verify(sessaoVotacaoRepository).save(sessao);
        assertNull(sessao.getDataAbertura());
        assertNull(sessao.getDataFechamento());
        assertNull(sessao.getStatus());
    }

    @Test
    @DisplayName("Deve listar sessões paginadas")
    void listarSessoes() {
        var pauta = new Pauta();
        pauta.setId(1L);
        var sessao = new SessaoVotacao();
        sessao.setId(1L);
        sessao.setPautas(java.util.List.of(pauta));
        sessao.setDataAbertura(LocalDateTime.now());
        sessao.setDataFechamento(LocalDateTime.now().plusMinutes(1));
        sessao.setStatus(StatusSessao.ABERTA);

        Page<SessaoVotacao> page = new PageImpl<>(Collections.singletonList(sessao));
        when(sessaoVotacaoRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<DadosListagemSessao> resultado = sessaoService.listarSessoes(PageRequest.of(0, 10));
        assertEquals(1, resultado.getContent().size());
        assertEquals(sessao.getId(), resultado.getContent().get(0).id());
    }

    @Test
    @DisplayName("Deve retornar id da pauta corretamente em listarSessoes")
    void listarSessoesRetornaIdPautaCorreto() {
        var pauta = new br.com.db.sicredi.votos.domain.pauta.Pauta();
        pauta.setId(123L);
        var sessao = new SessaoVotacao();
        sessao.setId(1L);
        sessao.setPautas(java.util.List.of(pauta));
        sessao.setDataAbertura(java.time.LocalDateTime.now());
        sessao.setDataFechamento(java.time.LocalDateTime.now().plusMinutes(1));
        sessao.setStatus(StatusSessao.ABERTA);

        org.springframework.data.domain.Page<SessaoVotacao> page = new org.springframework.data.domain.PageImpl<>(
                java.util.Collections.singletonList(sessao));
        when(sessaoVotacaoRepository.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(page);

        var resultado = sessaoService.listarSessoes(org.springframework.data.domain.PageRequest.of(0, 10));
        assertEquals(1, resultado.getContent().size());
        assertEquals(123L, resultado.getContent().get(0).pautaId());
    }

    @Test
    @DisplayName("Deve buscar sessão por id")
    void buscarPorId() {
        var sessao = new SessaoVotacao();
        sessao.setId(1L);
        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        SessaoVotacao resultado = sessaoService.buscarPorId(1L);
        assertEquals(1L, resultado.getId());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar sessão inexistente")
    void buscarPorIdInexistente() {
        when(sessaoVotacaoRepository.findById(99L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(IllegalArgumentException.class, () -> sessaoService.buscarPorId(99L));
        assertEquals("Sessão não encontrada", ex.getMessage());
    }

    @Test
    @DisplayName("Deve excluir sessão existente")
    void excluirSessao() {
        var sessao = new SessaoVotacao();
        sessao.setId(1L);
        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        sessaoService.excluirSessao(1L);
        verify(sessaoVotacaoRepository).delete(sessao);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir sessão inexistente")
    void excluirSessaoInexistente() {
        when(sessaoVotacaoRepository.findById(99L)).thenReturn(Optional.empty());
        Exception ex = assertThrows(IllegalArgumentException.class, () -> sessaoService.excluirSessao(99L));
        assertEquals("Sessão não encontrada", ex.getMessage());
    }

    @Test
    @DisplayName("Deve atualizar apenas a data de abertura")
    void atualizarSomenteDataAbertura() {
        var sessao = new SessaoVotacao();
        sessao.setId(1L);
        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        var novaData = LocalDateTime.now().plusMinutes(10);
        var dados = new DadosAtualizacaoSessao(1L, null, novaData, null, null);

        sessaoService.atualizarSessao(dados);

        assertEquals(novaData, sessao.getDataAbertura());
        assertNull(sessao.getDataFechamento());
        assertNull(sessao.getStatus());
        verify(sessaoVotacaoRepository).save(sessao);
    }

    @Test
    @DisplayName("Deve atualizar apenas a data de fechamento")
    void atualizarSomenteDataFechamento() {
        var sessao = new SessaoVotacao();
        sessao.setId(1L);
        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        var novaData = LocalDateTime.now().plusMinutes(20);
        var dados = new DadosAtualizacaoSessao(1L, null, null, novaData, null);

        sessaoService.atualizarSessao(dados);

        assertNull(sessao.getDataAbertura());
        assertEquals(novaData, sessao.getDataFechamento());
        assertNull(sessao.getStatus());
        verify(sessaoVotacaoRepository).save(sessao);
    }

    @Test
    @DisplayName("Deve atualizar apenas o status")
    void atualizarSomenteStatus() {
        var sessao = new SessaoVotacao();
        sessao.setId(1L);
        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        var dados = new DadosAtualizacaoSessao(1L, null, null, null, StatusSessao.FECHADA);

        sessaoService.atualizarSessao(dados);

        assertNull(sessao.getDataAbertura());
        assertNull(sessao.getDataFechamento());
        assertEquals(StatusSessao.FECHADA, sessao.getStatus());
        verify(sessaoVotacaoRepository).save(sessao);
    }

    @Test
    @DisplayName("Deve atualizar todos os campos da sessão")
    void atualizarTodosOsCampos() {
        var sessao = new SessaoVotacao();
        sessao.setId(1L);
        when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessao));
        var novaAbertura = LocalDateTime.now().plusMinutes(10);
        var novaFechamento = novaAbertura.plusMinutes(5);
        var dados = new DadosAtualizacaoSessao(1L, 1L, novaAbertura, novaFechamento, StatusSessao.FECHADA);

        sessaoService.atualizarSessao(dados);

        assertEquals(novaAbertura, sessao.getDataAbertura());
        assertEquals(novaFechamento, sessao.getDataFechamento());
        assertEquals(StatusSessao.FECHADA, sessao.getStatus());
        verify(sessaoVotacaoRepository).save(sessao);
    }

    @Test
    @DisplayName("Deve abrir sessão com data padrão se não informada")
    void abrirSessaoComDataPadrao() {
        var pauta = new Pauta();
        pauta.setId(1L);
        var dados = new DadosCadastroSessao(java.util.List.of(1L), null, null);
        when(pautaRepository.findAllById(java.util.List.of(1L))).thenReturn(java.util.List.of(pauta));
        when(sessaoVotacaoRepository.findByPautasIdAndStatus(1L, StatusSessao.ABERTA))
                .thenReturn(Collections.emptyList());
        when(sessaoVotacaoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        SessaoVotacao sessao = sessaoService.abrirSessao(dados);

        assertNotNull(sessao.getDataAbertura());
        assertNotNull(sessao.getDataFechamento());
        assertTrue(sessao.getDataFechamento().isAfter(sessao.getDataAbertura()));
        assertEquals(1, java.time.Duration.between(sessao.getDataAbertura(), sessao.getDataFechamento()).toMinutes());
    }

    @Test
    @DisplayName("Deve abrir sessão com datas informadas corretamente")
    void abrirSessaoComDatasInformadas() {
        var pauta = new Pauta();
        pauta.setId(1L);
        var abertura = LocalDateTime.now().plusMinutes(5);
        var fechamento = abertura.plusMinutes(10);
        var dados = new DadosCadastroSessao(java.util.List.of(1L), abertura, fechamento);
        when(pautaRepository.findAllById(java.util.List.of(1L))).thenReturn(java.util.List.of(pauta));
        when(sessaoVotacaoRepository.findByPautasIdAndStatus(1L, StatusSessao.ABERTA))
                .thenReturn(Collections.emptyList());
        when(sessaoVotacaoRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        SessaoVotacao sessao = sessaoService.abrirSessao(dados);

        assertEquals(abertura, sessao.getDataAbertura());
        assertEquals(fechamento, sessao.getDataFechamento());
    }
}