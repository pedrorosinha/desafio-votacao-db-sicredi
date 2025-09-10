package br.com.db.sicredi.votos.services.interf;

import br.com.db.sicredi.votos.domain.associado.Associado;
import br.com.db.sicredi.votos.domain.associado.DadosListagemAssociado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AssociadoService {
    Page<DadosListagemAssociado> listar(Pageable pageable);
    Optional<DadosListagemAssociado> buscarPorId(Long id);
    DadosListagemAssociado criar(Associado associado);
    boolean deletar(Long id);
}
