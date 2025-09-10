package br.com.db.sicredi.votos.domain.voto;

import java.time.LocalDateTime;

import br.com.db.sicredi.votos.domain.associado.Associado;
import br.com.db.sicredi.votos.domain.enums.EscolhaVoto;
import br.com.db.sicredi.votos.domain.pauta.Pauta;
import br.com.db.sicredi.votos.domain.sessaoVotacao.SessaoVotacao;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private SessaoVotacao sessao;
    @ManyToOne
    private Pauta pauta;
    @Enumerated(EnumType.STRING)
    private EscolhaVoto escolha;
    private LocalDateTime dataCriacao;
    @ManyToOne
    @JoinColumn(name = "associado_id", referencedColumnName = "id", nullable = false)
    private Associado associado;
}
