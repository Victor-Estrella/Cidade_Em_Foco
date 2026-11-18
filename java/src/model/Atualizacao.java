package model;

import java.time.LocalDateTime;

public class Atualizacao {
    private int idAtualizacao;
    private Usuario usuarioResponsavel;
    private String statusAnterior;
    private String statusNovo;
    private String descricao;
    private LocalDateTime dataAtualizacao;

    public Atualizacao(int idAtualizacao, Usuario usuarioResponsavel, String statusAnterior,
                       String statusNovo, String descricao, LocalDateTime dataAtualizacao) {
        this.idAtualizacao = idAtualizacao;
        this.usuarioResponsavel = usuarioResponsavel;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.descricao = descricao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public int getIdAtualizacao() { return idAtualizacao; }
    public void setIdAtualizacao(int idAtualizacao) { this.idAtualizacao = idAtualizacao; }

    public Usuario getUsuarioResponsavel() { return usuarioResponsavel; }
    public void setUsuarioResponsavel(Usuario usuarioResponsavel) { this.usuarioResponsavel = usuarioResponsavel; }

    public String getStatusAnterior() { return statusAnterior; }
    public void setStatusAnterior(String statusAnterior) { this.statusAnterior = statusAnterior; }

    public String getStatusNovo() { return statusNovo; }
    public void setStatusNovo(String statusNovo) { this.statusNovo = statusNovo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}
