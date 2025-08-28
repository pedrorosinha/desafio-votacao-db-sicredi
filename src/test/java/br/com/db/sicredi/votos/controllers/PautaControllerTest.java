package br.com.db.sicredi.votos.controllers;

import br.com.db.sicredi.votos.domain.pauta.DadosAtualizacaoPauta;
import br.com.db.sicredi.votos.domain.pauta.DadosCadastroPauta;
import br.com.db.sicredi.votos.services.interf.PautaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PautaController.class)
class PautaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PautaService pautaService;

    @Test
    @DisplayName("Deve retornar 200 ao cadastrar pauta")
    void cadastrarPauta() throws Exception {
        String json = """
            {
                "titulo": "Pauta Teste",
                "descricao": "Descrição da pauta teste"
            }
            """;

        mockMvc.perform(post("/pautas/cadastrar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        verify(pautaService).cadastrarPauta(any(DadosCadastroPauta.class));
    }

    @Test
    @DisplayName("Deve retornar 200 ao listar pautas")
    void listarPautas() throws Exception {
        when(pautaService.listarPautas(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/pautas")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());

        verify(pautaService).listarPautas(any(Pageable.class));
    }

    @Test
    @DisplayName("Deve retornar 200 ao atualizar pauta")
    void atualizarPauta() throws Exception {
        String json = """
            {
                "id": 1,
                "titulo": "Novo Titulo",
                "descricao": "Nova Descricao"
            }
            """;

        mockMvc.perform(put("/pautas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        verify(pautaService).atualizarPauta(any(DadosAtualizacaoPauta.class));
    }

    @Test
    @DisplayName("Deve retornar 200 ao excluir pauta")
    void excluirPauta() throws Exception {
        mockMvc.perform(delete("/pautas/1"))
                .andExpect(status().isOk());

        verify(pautaService).excluirPauta(1L);
    }
}
