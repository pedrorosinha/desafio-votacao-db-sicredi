package br.com.db.sicredi.votos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.db.sicredi.votos.domain.pauta.DadosAtualizacaoPauta;
import br.com.db.sicredi.votos.domain.pauta.DadosCadastroPauta;
import br.com.db.sicredi.votos.domain.pauta.Pauta;
import br.com.db.sicredi.votos.domain.pauta.PautaRepository;
import br.com.db.sicredi.votos.services.interf.PautaService;

@Service
public class PautaServiceImpl implements PautaService {

    @Autowired
    private PautaRepository pautaRepository;

    @Override
    public Pauta cadastrarPauta(DadosCadastroPauta dados) {
        Pauta pauta = new Pauta(dados);
        return pautaRepository.save(pauta);
    }

    @Override
    public Page<Pauta> listarPautas(Pageable pageable) {
        return pautaRepository.findAll(pageable);
    }

    @Override
    public void atualizarPauta(DadosAtualizacaoPauta dados) {
        Pauta pauta = pautaRepository.getReferenceById(dados.id());
        pauta.atualizarDados(dados);
    }

    @Override
    public void excluirPauta(Long id) {
        Pauta pauta = pautaRepository.getReferenceById(id);
        pautaRepository.delete(pauta);
    }
}