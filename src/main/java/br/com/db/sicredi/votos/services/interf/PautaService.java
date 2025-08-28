package br.com.db.sicredi.votos.services.interf;

import br.com.db.sicredi.votos.domain.pauta.DadosCadastroPauta;
import br.com.db.sicredi.votos.domain.pauta.DadosAtualizacaoPauta;
import br.com.db.sicredi.votos.domain.pauta.Pauta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PautaService {
    Pauta cadastrarPauta(DadosCadastroPauta dados);
    Page<Pauta> listarPautas(Pageable pageable);
    void atualizarPauta(DadosAtualizacaoPauta dados);
    void excluirPauta(Long id);
}