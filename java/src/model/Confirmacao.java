package model;

import java.time.LocalDateTime;

public class Confirmacao {
    private int idConfirmacao;
    private Usuario usuario;
    private LocalDateTime dataConfirmacao;

    public Confirmacao(int idConfirmacao, Usuario usuario, LocalDateTime dataConfirmacao) {
        this.idConfirmacao = idConfirmacao;
        this.usuario = usuario;
        this.dataConfirmacao = dataConfirmacao;
    }

    public int getIdConfirmacao() { return idConfirmacao; }
    public void setIdConfirmacao(int idConfirmacao) { this.idConfirmacao = idConfirmacao; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public LocalDateTime getDataConfirmacao() { return dataConfirmacao; }
    public void setDataConfirmacao(LocalDateTime dataConfirmacao) { this.dataConfirmacao = dataConfirmacao; }
}
