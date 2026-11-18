package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ocorrencia {
    private int idOcorrencia;
    private String titulo;
    private String descricao;
    private String localizacao;
    private int gravidade;
    private int pessoasAfetadas;
    private String status;
    private LocalDateTime dataRegistro;
    private Usuario usuario;
    private Categoria categoria;
    private List<Confirmacao> confirmacoes;
    private List<Atualizacao> atualizacoes;

    public Ocorrencia(int idOcorrencia, String titulo, String descricao, String localizacao,
                      int gravidade, int pessoasAfetadas, String status,
                      LocalDateTime dataRegistro, Usuario usuario, Categoria categoria) {
        this.idOcorrencia = idOcorrencia;
        this.titulo = titulo;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.gravidade = gravidade;
        this.pessoasAfetadas = pessoasAfetadas;
        this.status = status;
        this.dataRegistro = dataRegistro;
        this.usuario = usuario;
        this.categoria = categoria;
        this.confirmacoes = new ArrayList<>();
        this.atualizacoes = new ArrayList<>();
    }

    public int getIdOcorrencia() { return idOcorrencia; }
    public void setIdOcorrencia(int idOcorrencia) { this.idOcorrencia = idOcorrencia; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public int getGravidade() { return gravidade; }
    public void setGravidade(int gravidade) { this.gravidade = gravidade; }

    public int getPessoasAfetadas() { return pessoasAfetadas; }
    public void setPessoasAfetadas(int pessoasAfetadas) { this.pessoasAfetadas = pessoasAfetadas; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public List<Confirmacao> getConfirmacoes() { return confirmacoes; }
    public List<Atualizacao> getAtualizacoes() { return atualizacoes; }

    public int calcularPrioridade() {
        int pontosPessoas;
        if (pessoasAfetadas <= 5) pontosPessoas = 1;
        else if (pessoasAfetadas <= 20) pontosPessoas = 2;
        else if (pessoasAfetadas <= 50) pontosPessoas = 3;
        else pontosPessoas = 4;

        long dias = java.time.Duration.between(dataRegistro, LocalDateTime.now()).toDays();
        int pontosTempo = dias <= 7 ? 1 : (dias <= 30 ? 2 : 3);
        return gravidade + pontosPessoas + pontosTempo;
    }

    public String classificarPrioridade() {
        int pontos = calcularPrioridade();
        if (pontos <= 3) return "BAIXA";
        if (pontos <= 6) return "MEDIA";
        return "ALTA";
    }

    @Override
    public String toString() {
        return "ID: " + idOcorrencia
                + " | " + titulo
                + " | Categoria: " + categoria.getNome()
                + " | Local: " + localizacao
                + " | Gravidade: " + gravidade
                + " | Status: " + status
                + " | Prioridade: " + classificarPrioridade()
                + " (" + calcularPrioridade() + " pontos)"
                + " | Confirmações: " + confirmacoes.size();
    }
}
