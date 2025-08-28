package br.com.db.sicredi.votos.domain.pauta;

public record DadosListagemPauta(Long id, String titulo, String descricao) {
    public DadosListagemPauta(Pauta pauta) {
        this(pauta.getId(), pauta.getTitulo(), pauta.getDescricao());
    }
}
