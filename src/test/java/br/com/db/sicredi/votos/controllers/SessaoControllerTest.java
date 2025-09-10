package br.com.db.sicredi.votos.controllers;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.db.sicredi.votos.domain.sessaoVotacao.DadosAtualizacaoSessao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.DadosCadastroSessao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.SessaoVotacao;
import br.com.db.sicredi.votos.services.interf.SessaoService;

@WebMvcTest(SessaoController.class)
public class SessaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SessaoService sessaoService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("Deve retornar 200 ao abrir sessão")
    void abrirSessao() throws Exception {
        var now = LocalDateTime.now().plusMinutes(1);
        var dto = new br.com.db.sicredi.votos.domain.sessaoVotacao.DadosCadastroSessao(
                java.util.List.of(1L),
                now,
                now.plusMinutes(5));
        when(sessaoService.abrirSessao(any(DadosCadastroSessao.class))).thenReturn(new SessaoVotacao());
        mockMvc.perform(post("/sessoes/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
        verify(sessaoService).abrirSessao(any(DadosCadastroSessao.class));
    }

    @Test
    @DisplayName("Deve retornar 200 ao atualizar sessão")
    void atualizarSessao() throws Exception {
        var dto = new DadosAtualizacaoSessao(
                1L,
                1L,
                LocalDateTime.of(2025, 8, 28, 10, 0, 0),
                LocalDateTime.of(2025, 8, 28, 10, 5, 0),
                br.com.db.sicredi.votos.domain.enums.StatusSessao.ABERTA);
        mockMvc.perform(put("/sessoes/atualizar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
        verify(sessaoService).atualizarSessao(any(DadosAtualizacaoSessao.class));
    }

    @Test
    @DisplayName("Deve retornar 200 ao listar sessões")
    void listarSessoes() throws Exception {
        when(sessaoService.listarSessoes(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        mockMvc.perform(get("/sessoes")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
        verify(sessaoService).listarSessoes(any(Pageable.class));
    }

    @Test
    @DisplayName("Deve retornar 200 ao buscar sessão por id")
    void buscarPorId() throws Exception {
        when(sessaoService.buscarPorId(1L)).thenReturn(new SessaoVotacao());
        mockMvc.perform(get("/sessoes/1"))
                .andExpect(status().isOk());
        verify(sessaoService).buscarPorId(1L);
    }

    @Test
    @DisplayName("Deve retornar 200 ao excluir sessão")
    void excluirSessao() throws Exception {
        mockMvc.perform(delete("/sessoes/1"))
                .andExpect(status().isOk());
        verify(sessaoService).excluirSessao(1L);
    }
}