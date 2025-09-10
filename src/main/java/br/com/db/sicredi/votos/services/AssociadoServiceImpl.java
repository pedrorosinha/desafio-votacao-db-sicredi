package br.com.db.sicredi.votos.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.db.sicredi.votos.domain.associado.Associado;
import br.com.db.sicredi.votos.domain.associado.AssociadoRepository;
import br.com.db.sicredi.votos.domain.associado.DadosListagemAssociado;
import br.com.db.sicredi.votos.services.interf.AssociadoService;

@Service
public class AssociadoServiceImpl implements AssociadoService {

    @Autowired
    private AssociadoRepository associadoRepository;

    @Override
    public Page<DadosListagemAssociado> listar(Pageable pageable) {
        return associadoRepository.findAll(pageable)
                .map(associado -> new DadosListagemAssociado(associado.getId(), associado.getNome()));
    }

    @Override
    public Optional<DadosListagemAssociado> buscarPorId(Long id) {
        return associadoRepository.findById(id)
                .map(associado -> new DadosListagemAssociado(associado.getId(), associado.getNome()));
    }

    @Override
    public DadosListagemAssociado criar(Associado associado) {
        Associado salvo = associadoRepository.save(associado);
        return new DadosListagemAssociado(salvo.getId(), salvo.getNome());
    }

    @Override
    public boolean deletar(Long id) {
        if (associadoRepository.existsById(id)) {
            associadoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
