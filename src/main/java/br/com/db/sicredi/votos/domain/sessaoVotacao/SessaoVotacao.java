package br.com.db.sicredi.votos.domain.sessaoVotacao;

import java.time.LocalDateTime;

import br.com.db.sicredi.votos.domain.enums.StatusSessao;
import br.com.db.sicredi.votos.domain.pauta.Pauta;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SessaoVotacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;
    @Enumerated(EnumType.STRING)
    private StatusSessao status;

    @ManyToOne
    private Pauta pauta;

    public SessaoVotacao() {}

    public SessaoVotacao(Pauta pauta, LocalDateTime dataAbertura, LocalDateTime dataFechamento, StatusSessao status) {
        this.pauta = pauta;
        this.dataAbertura = dataAbertura;
        this.dataFechamento = dataFechamento;
        this.status = status;
    }
}