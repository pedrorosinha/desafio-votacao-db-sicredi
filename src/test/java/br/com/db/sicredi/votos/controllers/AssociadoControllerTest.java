package br.com.db.sicredi.votos.controllers;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.db.sicredi.votos.domain.associado.Associado;
import br.com.db.sicredi.votos.domain.associado.AssociadoRepository;

@WebMvcTest(AssociadoController.class)
class AssociadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssociadoRepository associadoRepository;

    @Test
    void criarAssociadoReturnsOkAndBody() throws Exception {
        Associado a = new Associado();
        a.setId(1L);
        a.setNome("Fulano");
        when(associadoRepository.save(org.mockito.ArgumentMatchers.any(Associado.class))).thenReturn(a);

        mockMvc.perform(post("/associados")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Fulano\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Fulano"));
    }

    @Test
    void listarRetornaPagina() throws Exception {
        Associado a = new Associado();
        a.setId(2L);
        a.setNome("Beltrano");
        Page<Associado> page = new PageImpl<>(List.of(a), PageRequest.of(0, 10), 1);
        when(associadoRepository.findAll(org.mockito.ArgumentMatchers.any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/associados").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(2))
                .andExpect(jsonPath("$.content[0].nome").value("Beltrano"));
    }

    @Test
    void atualizarRetornaOkWhenExists() throws Exception {
        Associado a = new Associado();
        a.setId(3L);
        a.setNome("Carlos");
        when(associadoRepository.existsById(3L)).thenReturn(true);
        when(associadoRepository.save(org.mockito.ArgumentMatchers.any(Associado.class))).thenReturn(a);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/associados/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"Carlos\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.nome").value("Carlos"));
    }

    @Test
    void atualizarRetornaNotFoundWhenIdNaoExiste() throws Exception {
        when(associadoRepository.existsById(99L)).thenReturn(false);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/associados/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nome\":\"NaoExiste\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorIdReturnsOkWhenFoundAndNotFoundOtherwise() throws Exception {
        Associado a = new Associado();
        a.setId(6L);
        a.setNome("Dante");
        when(associadoRepository.findById(6L)).thenReturn(java.util.Optional.of(a));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/associados/6")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.nome").value("Dante"));

        when(associadoRepository.findById(7L)).thenReturn(java.util.Optional.empty());
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/associados/7")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletarNoContentWhenExistsElseNotFound() throws Exception {
        when(associadoRepository.existsById(4L)).thenReturn(true);

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/associados/4"))
                .andExpect(status().isNoContent());

        when(associadoRepository.existsById(5L)).thenReturn(false);
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/associados/5"))
                .andExpect(status().isNotFound());
    }
}
