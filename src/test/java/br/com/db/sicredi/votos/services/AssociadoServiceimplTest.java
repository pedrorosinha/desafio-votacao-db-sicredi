package br.com.db.sicredi.votos.services;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import br.com.db.sicredi.votos.domain.associado.Associado;
import br.com.db.sicredi.votos.domain.associado.AssociadoRepository;
import br.com.db.sicredi.votos.domain.associado.DadosListagemAssociado;

@ExtendWith(MockitoExtension.class)
class AssociadoServiceimplTest {

	@Mock
	AssociadoRepository associadoRepository;

	@InjectMocks
	AssociadoServiceImpl associadoService;

	@Test
	@DisplayName("Deve listar associados paginados")
	void listarAssociados() {
		var associado = new Associado("João");
		associado.setId(1L);
		Page<Associado> page = new PageImpl<>(java.util.List.of(associado));
		when(associadoRepository.findAll(any(PageRequest.class))).thenReturn(page);

		var resultado = associadoService.listar(PageRequest.of(0, 10));
		assertEquals(1, resultado.getContent().size());
		DadosListagemAssociado dto = resultado.getContent().get(0);
		assertEquals(1L, dto.id());
		assertEquals("João", dto.nome());
	}

	@Test
	@DisplayName("Deve buscar associado por id")
	void buscarPorId() {
		var associado = new Associado("Maria");
		associado.setId(2L);
		when(associadoRepository.findById(2L)).thenReturn(Optional.of(associado));

		var resultado = associadoService.buscarPorId(2L);
		assertTrue(resultado.isPresent());
		assertEquals("Maria", resultado.get().nome());
	}

	@Test
	@DisplayName("Deve criar associado")
	void criarAssociado() {
		var associado = new Associado("Pedro");
		associado.setId(3L);
		when(associadoRepository.save(any(Associado.class))).thenReturn(associado);

		var dto = associadoService.criar(new Associado("Pedro"));
		assertEquals(3L, dto.id());
		assertEquals("Pedro", dto.nome());
	}

	@Test
	@DisplayName("Deve deletar associado existente")
	void deletarAssociado() {
		when(associadoRepository.existsById(4L)).thenReturn(true);
		boolean ok = associadoService.deletar(4L);
		assertTrue(ok);
		verify(associadoRepository).deleteById(4L);
	}

	@Test
	@DisplayName("Não deve deletar associado inexistente")
	void naoDeletarAssociadoInexistente() {
		when(associadoRepository.existsById(99L)).thenReturn(false);
		boolean ok = associadoService.deletar(99L);
		assertFalse(ok);
		verify(associadoRepository, never()).deleteById(99L);
	}
}
