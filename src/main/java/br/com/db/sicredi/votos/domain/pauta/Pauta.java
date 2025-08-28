package br.com.db.sicredi.votos.domain.pauta;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.db.sicredi.votos.domain.sessaoVotacao.SessaoVotacao;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pauta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descricao;
    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "pauta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<SessaoVotacao> sessaoVotacao;

    public Pauta() {
    }

    public Pauta(DadosCadastroPauta dados) {
        this.titulo = dados.titulo();
        this.descricao = dados.descricao();
        this.dataCriacao = LocalDateTime.now();
    }

    public void atualizarDados(DadosAtualizacaoPauta dados) {
        if (dados.titulo() != null) {
            this.titulo = dados.titulo();
        }
        if (dados.descricao() != null) {
            this.descricao = dados.descricao();
        }
    }
}
