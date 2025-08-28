package br.com.db.sicredi.votos.services;

import br.com.db.sicredi.votos.domain.pauta.DadosAtualizacaoPauta;
import br.com.db.sicredi.votos.domain.pauta.DadosCadastroPauta;
import br.com.db.sicredi.votos.domain.pauta.Pauta;
import br.com.db.sicredi.votos.domain.pauta.PautaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PautaServiceImplTest {

    @Mock
    private PautaRepository pautaRepository;

    @InjectMocks
    private PautaServiceImpl pautaService;

    @Test
    @DisplayName("Deve cadastrar uma pauta corretamente")
    public void testCadastrarPauta() {
        DadosCadastroPauta dados = new DadosCadastroPauta("Título Teste", "Descrição Teste");
        Pauta pautaSalva = new Pauta(dados);

        when(pautaRepository.save(any(Pauta.class))).thenReturn(pautaSalva);

        Pauta resultado = pautaService.cadastrarPauta(dados);

        assertNotNull(resultado);
        assertEquals("Título Teste", resultado.getTitulo());
        assertEquals("Descrição Teste", resultado.getDescricao());
        verify(pautaRepository, times(1)).save(any(Pauta.class));
    }

    @Test
    @DisplayName("Deve listar pautas paginadas")
    public void testListarPautas() {
        Pauta pauta1 = new Pauta(new DadosCadastroPauta("Pauta 1", "Descricao 1"));
        Pauta pauta2 = new Pauta(new DadosCadastroPauta("Pauta 2", "Descricao 2"));
        Page<Pauta> page = new PageImpl<>(Arrays.asList(pauta1, pauta2));
        when(pautaRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Pauta> resultado = pautaService.listarPautas(PageRequest.of(0, 10));

        assertEquals(2, resultado.getContent().size());
        verify(pautaRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    @DisplayName("Deve atualizar uma pauta existente")
    public void testAtualizarPauta() {
        DadosAtualizacaoPauta dados = new DadosAtualizacaoPauta(1L, "Novo Titulo", "Nova Descricao");
        Pauta pauta = new Pauta(new DadosCadastroPauta("Antigo", "Antigo"));
        when(pautaRepository.getReferenceById(1L)).thenReturn(pauta);

        pautaService.atualizarPauta(dados);

        assertEquals("Novo Titulo", pauta.getTitulo());
        assertEquals("Nova Descricao", pauta.getDescricao());
        verify(pautaRepository, times(1)).getReferenceById(1L);
    }

    @Test
    @DisplayName("Deve excluir uma pauta existente")
    public void testExcluirPauta() {
        Pauta pauta = new Pauta(new DadosCadastroPauta("Excluir", "Excluir"));
        when(pautaRepository.getReferenceById(1L)).thenReturn(pauta);

        pautaService.excluirPauta(1L);

        verify(pautaRepository, times(1)).getReferenceById(1L);
        verify(pautaRepository, times(1)).delete(pauta);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar pauta inexistente")
    public void testAtualizarPautaInexistente() {
        DadosAtualizacaoPauta dados = new DadosAtualizacaoPauta(99L, "Novo", "Nova");
        when(pautaRepository.getReferenceById(99L)).thenThrow(new IllegalArgumentException("Pauta não encontrada"));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> pautaService.atualizarPauta(dados));
        assertEquals("Pauta não encontrada", ex.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir pauta inexistente")
    public void testExcluirPautaInexistente() {
        when(pautaRepository.getReferenceById(99L)).thenThrow(new IllegalArgumentException("Pauta não encontrada"));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> pautaService.excluirPauta(99L));
        assertEquals("Pauta não encontrada", ex.getMessage());
    }
}