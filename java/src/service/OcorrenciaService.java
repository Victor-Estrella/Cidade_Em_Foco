package service;

import model.Atualizacao;
import model.Categoria;
import model.Confirmacao;
import model.Ocorrencia;
import model.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OcorrenciaService {
    private List<Ocorrencia> ocorrencias;
    private List<Categoria> categorias;
    private int proximoIdOcorrencia;
    private int proximoIdConfirmacao;
    private int proximoIdAtualizacao;

    public OcorrenciaService() {
        ocorrencias = new ArrayList<>();
        categorias = new ArrayList<>();
        proximoIdOcorrencia = 1;
        proximoIdConfirmacao = 1;
        proximoIdAtualizacao = 1;
        carregarCategorias();
    }

    private void carregarCategorias() {
        categorias.add(new Categoria(1, "Infraestrutura viaria", "Buracos, ruas e pavimentacao."));
        categorias.add(new Categoria(2, "Iluminacao publica", "Postes e luminarias."));
        categorias.add(new Categoria(3, "Limpeza urbana", "Lixo acumulado e descarte irregular."));
        categorias.add(new Categoria(4, "Acessibilidade", "Calcadas e rampas."));
        categorias.add(new Categoria(5, "Espacos publicos", "Pracas e areas de convivencia."));
    }

    public List<Categoria> listarCategorias() {
        return new ArrayList<>(categorias);
    }

    public void cadastrar(Ocorrencia ocorrencia) {
        validarOcorrencia(ocorrencia);
        ocorrencia.setIdOcorrencia(proximoIdOcorrencia++);
        ocorrencia.setDataRegistro(LocalDateTime.now());
        ocorrencias.add(ocorrencia);
    }

    public List<Ocorrencia> listarTodas() {
        return new ArrayList<>(ocorrencias);
    }

    public Ocorrencia buscarPorId(int id) {
        for (Ocorrencia ocorrencia : ocorrencias) {
            if (ocorrencia.getIdOcorrencia() == id) return ocorrencia;
        }
        return null;
    }

    public void atualizar(int id, String titulo, String descricao, String localizacao,
                          int gravidade, int pessoasAfetadas, Categoria categoria) {
        Ocorrencia ocorrencia = buscarPorId(id);
        if (ocorrencia == null) throw new IllegalArgumentException("Ocorrencia nao encontrada.");
        validarTexto(titulo, "Titulo");
        validarTexto(descricao, "Descricao");
        validarTexto(localizacao, "Localizacao");
        if (gravidade < 1 || gravidade > 3) throw new IllegalArgumentException("A gravidade deve ser de 1 a 3.");
        if (pessoasAfetadas < 1) throw new IllegalArgumentException("Pessoas afetadas deve ser maior que zero.");
        if (categoria == null) throw new IllegalArgumentException("Selecione uma categoria.");

        ocorrencia.setTitulo(titulo);
        ocorrencia.setDescricao(descricao);
        ocorrencia.setLocalizacao(localizacao);
        ocorrencia.setGravidade(gravidade);
        ocorrencia.setPessoasAfetadas(pessoasAfetadas);
        ocorrencia.setCategoria(categoria);
    }

    public void remover(int id) {
        Ocorrencia ocorrencia = buscarPorId(id);
        if (ocorrencia == null) throw new IllegalArgumentException("Ocorrencia nao encontrada.");
        ocorrencias.remove(ocorrencia);
    }

    public void atualizarStatus(int id, String novoStatus, String descricao, Usuario responsavel) {
        Ocorrencia ocorrencia = buscarPorId(id);
        if (ocorrencia == null) throw new IllegalArgumentException("Ocorrencia nao encontrada.");
        if (!statusValido(novoStatus)) {
            throw new IllegalArgumentException("Status invalido. Use ABERTA, EM_ANALISE, EM_ATENDIMENTO ou RESOLVIDA.");
        }
        validarTexto(descricao, "Descricao da atualizacao");

        String statusAnterior = ocorrencia.getStatus();
        ocorrencia.setStatus(novoStatus);
        ocorrencia.getAtualizacoes().add(new Atualizacao(
                proximoIdAtualizacao++, responsavel, statusAnterior, novoStatus,
                descricao, LocalDateTime.now()));
    }

    public void confirmar(int id, Usuario usuario) {
        Ocorrencia ocorrencia = buscarPorId(id);
        if (ocorrencia == null) throw new IllegalArgumentException("Ocorrencia nao encontrada.");
        for (Confirmacao confirmacao : ocorrencia.getConfirmacoes()) {
            if (confirmacao.getUsuario().getIdUsuario() == usuario.getIdUsuario()) {
                throw new IllegalArgumentException("Este usuario ja confirmou esta ocorrencia.");
            }
        }
        ocorrencia.getConfirmacoes().add(new Confirmacao(
                proximoIdConfirmacao++, usuario, LocalDateTime.now()));
        ocorrencia.setPessoasAfetadas(ocorrencia.getPessoasAfetadas() + 1);
    }

    private void validarOcorrencia(Ocorrencia ocorrencia) {
        if (ocorrencia == null) throw new IllegalArgumentException("Ocorrencia invalida.");
        validarTexto(ocorrencia.getTitulo(), "Titulo");
        validarTexto(ocorrencia.getDescricao(), "Descricao");
        validarTexto(ocorrencia.getLocalizacao(), "Localizacao");
        if (ocorrencia.getGravidade() < 1 || ocorrencia.getGravidade() > 3) {
            throw new IllegalArgumentException("A gravidade deve ser de 1 a 3.");
        }
        if (ocorrencia.getPessoasAfetadas() < 1) {
            throw new IllegalArgumentException("Pessoas afetadas deve ser maior que zero.");
        }
        if (ocorrencia.getUsuario() == null || ocorrencia.getCategoria() == null) {
            throw new IllegalArgumentException("Usuario e categoria sao obrigatorios.");
        }
    }

    private void validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " nao pode ficar vazio.");
        }
    }

    private boolean statusValido(String status) {
        return status != null && (status.equals("ABERTA") || status.equals("EM_ANALISE")
                || status.equals("EM_ATENDIMENTO") || status.equals("RESOLVIDA"));
    }
}
