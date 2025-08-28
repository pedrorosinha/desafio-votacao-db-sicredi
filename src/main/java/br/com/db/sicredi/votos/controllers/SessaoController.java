package br.com.db.sicredi.votos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import br.com.db.sicredi.votos.domain.sessaoVotacao.DadosAtualizacaoSessao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.DadosCadastroSessao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.DadosListagemSessao;
import br.com.db.sicredi.votos.domain.sessaoVotacao.SessaoVotacao;
import br.com.db.sicredi.votos.services.interf.SessaoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/sessoes")
public class SessaoController {

    @Autowired
    private SessaoService sessaoService;

    @PostMapping("/cadastrar")
    @Transactional
    public SessaoVotacao abrirSessao(@RequestBody @Valid DadosCadastroSessao dados) {
        return sessaoService.abrirSessao(dados);
    }

    @PutMapping("/atualizar")
    @Transactional
    public void atualizarSessao(@RequestBody @Valid DadosAtualizacaoSessao dados) {
        sessaoService.atualizarSessao(dados);
    }

    @GetMapping
    public Page<DadosListagemSessao> listar(@PageableDefault(size = 10) Pageable pageable) {
        return sessaoService.listarSessoes(pageable);
    }

    @GetMapping("/{id}")
    public SessaoVotacao buscarPorId(@PathVariable Long id) {
        return sessaoService.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    public void excluirSessao(@PathVariable Long id) {
        sessaoService.excluirSessao(id);
    }   
}
