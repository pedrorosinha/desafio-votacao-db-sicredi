package br.com.db.sicredi.votos.controllers;

import br.com.db.sicredi.votos.domain.enums.EscolhaVoto;
import br.com.db.sicredi.votos.domain.voto.DadosCadastroVoto;
import br.com.db.sicredi.votos.domain.voto.ResultadoVotacao;
import br.com.db.sicredi.votos.domain.voto.StatusResultadoVotos;
import br.com.db.sicredi.votos.services.interf.VotoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VotoController.class)
public class VotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VotoService votoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve retornar 200 ao votar")
    void votar() throws Exception {
        var dto = new DadosCadastroVoto(1L, EscolhaVoto.SIM);
        mockMvc.perform(post("/votos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
        verify(votoService).registrarVoto(any(DadosCadastroVoto.class));
    }

    @Test
    @DisplayName("Deve retornar 400 ao votar e service lançar exceção de negócio")
    void votarServiceErroNegocio() throws Exception {
        var dto = new DadosCadastroVoto(1L, EscolhaVoto.SIM);
        doThrow(new IllegalStateException("Voto já registrado")).when(votoService).registrarVoto(any(DadosCadastroVoto.class));
        mockMvc.perform(post("/votos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
        verify(votoService).registrarVoto(any(DadosCadastroVoto.class));
    }

    @Test
    @DisplayName("Deve retornar 200 ao consultar resultado")
    void resultado() throws Exception {
        when(votoService.contabilizarVotos(1L))
                .thenReturn(new ResultadoVotacao(1L, 10, 7, 3, StatusResultadoVotos.APROVADA));
        mockMvc.perform(get("/votos/resultado/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pautaId").value(1L))
                .andExpect(jsonPath("$.totalVotos").value(10))
                .andExpect(jsonPath("$.votosSim").value(7))
                .andExpect(jsonPath("$.votosNao").value(3))
                .andExpect(jsonPath("$.resultado").value("APROVADA"));
        verify(votoService).contabilizarVotos(1L);
    }

    @Test
    @DisplayName("Deve retornar 400 ao consultar resultado e service lançar exceção de negócio")
    void resultadoServiceErroNegocio() throws Exception {
        when(votoService.contabilizarVotos(1L)).thenThrow(new IllegalStateException("Pauta não encontrada"));
        mockMvc.perform(get("/votos/resultado/1"))
                .andExpect(status().isBadRequest());
        verify(votoService).contabilizarVotos(1L);
    }
}