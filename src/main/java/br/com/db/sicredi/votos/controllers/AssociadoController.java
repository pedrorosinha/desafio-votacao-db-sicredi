package br.com.db.sicredi.votos.controllers;

import br.com.db.sicredi.votos.domain.associado.Associado;
import br.com.db.sicredi.votos.domain.associado.AssociadoRepository;
import br.com.db.sicredi.votos.domain.associado.DadosListagemAssociado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/associados")
public class AssociadoController {

    @Autowired
    private AssociadoRepository associadoRepository;

    @GetMapping
    public Page<DadosListagemAssociado> listar(Pageable pageable) {
        return associadoRepository.findAll(pageable)
                .map(associado -> new DadosListagemAssociado(associado.getId(), associado.getNome()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosListagemAssociado> buscarPorId(@PathVariable Long id) {
        return associadoRepository.findById(id)
                .map(associado -> ResponseEntity.ok(new DadosListagemAssociado(associado.getId(), associado.getNome())))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DadosListagemAssociado> criar(@RequestBody Associado associado) {
        Associado salvo = associadoRepository.save(associado);
        return ResponseEntity.ok(new DadosListagemAssociado(salvo.getId(), salvo.getNome()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DadosListagemAssociado> atualizar(@PathVariable Long id, @RequestBody Associado associado) {
        if (!associadoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        associado.setId(id);
        Associado salvo = associadoRepository.save(associado);
        return ResponseEntity.ok(new DadosListagemAssociado(salvo.getId(), salvo.getNome()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (associadoRepository.existsById(id)) {
            associadoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
