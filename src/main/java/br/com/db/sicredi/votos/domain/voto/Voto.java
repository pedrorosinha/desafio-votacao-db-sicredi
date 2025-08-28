package br.com.db.sicredi.votos.domain.voto;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import br.com.db.sicredi.votos.domain.sessaoVotacao.SessaoVotacao;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import br.com.db.sicredi.votos.domain.enums.EscolhaVoto;

@Entity
@Getter
@Setter
public class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private SessaoVotacao sessao;
    @Enumerated(EnumType.STRING)
    private EscolhaVoto escolha;
    private LocalDateTime dataCriacao;
}
