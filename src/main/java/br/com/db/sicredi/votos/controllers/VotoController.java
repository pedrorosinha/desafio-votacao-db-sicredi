package br.com.db.sicredi.votos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import br.com.db.sicredi.votos.domain.voto.DadosCadastroVoto;
import br.com.db.sicredi.votos.domain.voto.ResultadoVotacao;
import br.com.db.sicredi.votos.services.interf.VotoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/votos")
public class VotoController {

    @Autowired
    private VotoService votoService;

    @PostMapping
    @Transactional
    public void votar(@RequestBody @Valid DadosCadastroVoto dados) {
        votoService.registrarVoto(dados);
    }

    @GetMapping("/resultado/{pautaId}")
    public ResultadoVotacao resultado(@PathVariable Long pautaId) {
        return votoService.contabilizarVotos(pautaId);
    }
}
