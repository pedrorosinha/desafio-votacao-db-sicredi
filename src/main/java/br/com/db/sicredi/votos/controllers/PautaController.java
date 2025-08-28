package br.com.db.sicredi.votos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.db.sicredi.votos.domain.pauta.DadosAtualizacaoPauta;
import br.com.db.sicredi.votos.domain.pauta.DadosCadastroPauta;
import br.com.db.sicredi.votos.domain.pauta.Pauta;
import br.com.db.sicredi.votos.services.interf.PautaService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/pautas")
public class PautaController {

    @Autowired
    private PautaService pautaService;

     @PostMapping("/cadastrar")
     @Transactional
     public void cadastrar(@RequestBody @Valid DadosCadastroPauta dados) {
         pautaService.cadastrarPauta(dados);
     }

     @GetMapping
     public Page<Pauta> listar(@PageableDefault(size = 10) Pageable pageable) {
         return pautaService.listarPautas(pageable);
     }

     @PutMapping
     @Transactional
     public void atualizar(@RequestBody @Valid DadosAtualizacaoPauta dados) {
        pautaService.atualizarPauta(dados);
     }

     @DeleteMapping("/{id}")
     @Transactional
     public void excluir(@PathVariable Long id) {
        pautaService.excluirPauta(id);
    }

}
