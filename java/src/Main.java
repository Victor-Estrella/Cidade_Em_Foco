import model.Categoria;
import model.Ocorrencia;
import model.Usuario;
import service.OcorrenciaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final OcorrenciaService service = new OcorrenciaService();
    private static final Usuario usuarioAtual =
            new Usuario(1, "Cidadao Demonstracao", "cidadao@cidadeemfoco.com",
                    "hash_demo", "Tatuape", "CIDADAO");

    public static void main(String[] args) {
        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opcao: ");
            try {
                switch (opcao) {
                    case 1: cadastrarOcorrencia(); break;
                    case 2: listarOcorrencias(); break;
                    case 3: buscarOcorrencia(); break;
                    case 4: atualizarOcorrencia(); break;
                    case 5: atualizarStatus(); break;
                    case 6: confirmarOcorrencia(); break;
                    case 7: removerOcorrencia(); break;
                    case 0: System.out.println("\nObrigado por utilizar o Cidade em Foco!"); break;
                    default: System.out.println("Opcao invalida. Escolha uma opcao do menu.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Nao foi possivel concluir: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ocorreu um erro inesperado. Tente novamente.");
            }
        } while (opcao != 0);
        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println("\n==============================================");
        System.out.println("                 CIDADE EM FOCO");
        System.out.println("    A cidade melhora quando todos participam.");
        System.out.println("==============================================");
        System.out.println("1 - Cadastrar ocorrencia");
        System.out.println("2 - Listar ocorrencias");
        System.out.println("3 - Buscar ocorrencia por ID");
        System.out.println("4 - Atualizar dados da ocorrencia");
        System.out.println("5 - Atualizar status");
        System.out.println("6 - Confirmar ocorrencia");
        System.out.println("7 - Remover ocorrencia (extra)");
        System.out.println("0 - Sair");
    }

    private static void cadastrarOcorrencia() {
        System.out.println("\n--- CADASTRO DE OCORRENCIA ---");
        String titulo = lerTexto("Titulo: ");
        String descricao = lerTexto("Descricao: ");
        String localizacao = lerTexto("Localizacao (rua, numero, bairro): ");
        int gravidade = lerInteiroNoIntervalo("Gravidade (1 a 3): ", 1, 3);
        int pessoas = lerInteiroMinimo("Quantidade inicial de pessoas afetadas: ", 1);
        Categoria categoria = selecionarCategoria();

        Ocorrencia ocorrencia = new Ocorrencia(0, titulo, descricao, localizacao,
                gravidade, pessoas, "ABERTA", LocalDateTime.now(), usuarioAtual, categoria);
        service.cadastrar(ocorrencia);
        System.out.println("Ocorrencia cadastrada com sucesso! ID: " + ocorrencia.getIdOcorrencia());
    }

    private static void listarOcorrencias() {
        System.out.println("\n--- OCORRENCIAS CADASTRADAS ---");
        List<Ocorrencia> lista = service.listarTodas();
        if (lista.isEmpty()) {
            System.out.println("Nenhuma ocorrencia cadastrada.");
            return;
        }
        for (Ocorrencia ocorrencia : lista) {
            System.out.println("\n----------------------------------------");
            System.out.println("OCORRENCIA #" + ocorrencia.getIdOcorrencia());
            System.out.println("Titulo: " + ocorrencia.getTitulo());
            System.out.println("Categoria: " + ocorrencia.getCategoria().getNome());
            System.out.println("Local: " + ocorrencia.getLocalizacao());
            System.out.println("Gravidade: " + ocorrencia.getGravidade());
            System.out.println("Status: " + ocorrencia.getStatus());
            System.out.println("Prioridade: " + ocorrencia.classificarPrioridade()
                    + " (" + ocorrencia.calcularPrioridade() + " pontos)");
            System.out.println("Confirmacoes: " + ocorrencia.getConfirmacoes().size());
        }
        System.out.println("----------------------------------------");
    }

    private static void buscarOcorrencia() {
        int id = lerInteiroMinimo("Informe o ID da ocorrencia: ", 1);
        Ocorrencia ocorrencia = service.buscarPorId(id);
        if (ocorrencia == null) {
            System.out.println("Ocorrencia nao encontrada.");
        } else {
            exibirDetalhes(ocorrencia);
        }
    }

    private static void atualizarOcorrencia() {
        int id = lerInteiroMinimo("ID da ocorrencia que deseja atualizar: ", 1);
        if (service.buscarPorId(id) == null) {
            System.out.println("Ocorrencia nao encontrada.");
            return;
        }
        Ocorrencia atual = service.buscarPorId(id);
        System.out.println("\nDeixe o campo vazio e pressione Enter para manter o valor atual.");

        String titulo = lerTextoOpcional("Titulo atual: " + atual.getTitulo() + "\nNovo titulo: ", atual.getTitulo());
        String descricao = lerTextoOpcional("Descricao atual: " + atual.getDescricao() + "\nNova descricao: ", atual.getDescricao());
        String localizacao = lerTextoOpcional("Localizacao atual: " + atual.getLocalizacao() + "\nNova localizacao: ", atual.getLocalizacao());
        int gravidade = lerInteiroOpcionalNoIntervalo(
                "Gravidade atual: " + atual.getGravidade() + "\nNova gravidade (1 a 3): ",
                atual.getGravidade(), 1, 3);
        int pessoas = lerInteiroOpcionalMinimo(
                "Pessoas afetadas atuais: " + atual.getPessoasAfetadas() + "\nNova quantidade: ",
                atual.getPessoasAfetadas(), 1);
        Categoria categoria = selecionarCategoriaOpcional(atual.getCategoria());

        service.atualizar(id, titulo, descricao, localizacao, gravidade, pessoas, categoria);
        System.out.println("Ocorrencia atualizada com sucesso!");
    }

    private static void atualizarStatus() {
        int id = lerInteiroMinimo("ID da ocorrencia: ", 1);
        String status = lerTexto("Novo status (ABERTA, EM_ANALISE, EM_ATENDIMENTO, RESOLVIDA): ")
                .trim().toUpperCase().replace(' ', '_');
        String descricao = lerTexto("Descricao da atualizacao: ");
        service.atualizarStatus(id, status, descricao, usuarioAtual);
        System.out.println("Status atualizado e historico registrado!");
    }

    private static void confirmarOcorrencia() {
        int id = lerInteiroMinimo("ID da ocorrencia que deseja confirmar: ", 1);
        service.confirmar(id, usuarioAtual);
        System.out.println("Confirmacao registrada! Pessoas afetadas atualizadas.");
    }

    private static void removerOcorrencia() {
        int id = lerInteiroMinimo("ID da ocorrencia que deseja remover: ", 1);
        String resposta = lerTexto("Tem certeza que deseja remover? (S/N): ");
        if (resposta.equalsIgnoreCase("S")) {
            service.remover(id);
            System.out.println("Ocorrencia removida.");
        } else {
            System.out.println("Remocao cancelada.");
        }
    }

    private static Categoria selecionarCategoria() {
        System.out.println("\nCategorias:");
        for (Categoria categoria : service.listarCategorias()) {
            System.out.println(categoria);
        }
        int id = lerInteiroMinimo("ID da categoria: ", 1);
        for (Categoria categoria : service.listarCategorias()) {
            if (categoria.getIdCategoria() == id) return categoria;
        }
        throw new IllegalArgumentException("Categoria inexistente.");
    }

    private static void exibirDetalhes(Ocorrencia o) {
        System.out.println("\n--- DETALHES DA OCORRENCIA ---");
        System.out.println("ID: " + o.getIdOcorrencia());
        System.out.println("Titulo: " + o.getTitulo());
        System.out.println("Descricao: " + o.getDescricao());
        System.out.println("Localizacao: " + o.getLocalizacao());
        System.out.println("Categoria: " + o.getCategoria().getNome());
        System.out.println("Gravidade: " + o.getGravidade());
        System.out.println("Pessoas afetadas: " + o.getPessoasAfetadas());
        System.out.println("Status: " + o.getStatus());
        System.out.println("Prioridade: " + o.classificarPrioridade()
                + " (" + o.calcularPrioridade() + " pontos)");
        System.out.println("Registrada por: " + o.getUsuario().getNome());
        System.out.println("Confirmacoes: " + o.getConfirmacoes().size());
        System.out.println("Atualizacoes: " + o.getAtualizacoes().size());
    }

    private static Categoria selecionarCategoriaOpcional(Categoria atual) {
        System.out.println("\nCategoria atual: " + atual.getNome());
        System.out.println("Digite o ID de uma nova categoria ou pressione Enter para manter:");
        for (Categoria categoria : service.listarCategorias()) {
            System.out.println(categoria);
        }
        while (true) {
            System.out.print("ID da categoria: ");
            String valor = scanner.nextLine().trim();
            if (valor.isEmpty()) return atual;
            try {
                int id = Integer.parseInt(valor);
                for (Categoria categoria : service.listarCategorias()) {
                    if (categoria.getIdCategoria() == id) return categoria;
                }
                System.out.println("Categoria inexistente. Digite um ID da lista ou Enter para manter.");
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro valido ou pressione Enter para manter.");
            }
        }
    }

    private static String lerTextoOpcional(String mensagem, String valorAtual) {
        System.out.print(mensagem);
        String valor = scanner.nextLine().trim();
        return valor.isEmpty() ? valorAtual : valor;
    }

    private static int lerInteiroOpcionalNoIntervalo(String mensagem, int valorAtual,
                                                      int minimo, int maximo) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();
            if (valor.isEmpty()) return valorAtual;
            try {
                int numero = Integer.parseInt(valor);
                if (numero >= minimo && numero <= maximo) return numero;
                System.out.println("Digite um valor entre " + minimo + " e " + maximo + ".");
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro valido ou pressione Enter para manter.");
            }
        }
    }

    private static int lerInteiroOpcionalMinimo(String mensagem, int valorAtual, int minimo) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();
            if (valor.isEmpty()) return valorAtual;
            try {
                int numero = Integer.parseInt(valor);
                if (numero >= minimo) return numero;
                System.out.println("O valor deve ser maior ou igual a " + minimo + ".");
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro valido ou pressione Enter para manter.");
            }
        }
    }

    private static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();
            if (!valor.isEmpty()) return valor;
            System.out.println("Este campo e obrigatorio. Tente novamente.");
        }
    }

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String valor = scanner.nextLine().trim();
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero inteiro valido.");
            }
        }
    }

    private static int lerInteiroMinimo(String mensagem, int minimo) {
        while (true) {
            int valor = lerInteiro(mensagem);
            if (valor >= minimo) return valor;
            System.out.println("O valor deve ser maior ou igual a " + minimo + ".");
        }
    }

    private static int lerInteiroNoIntervalo(String mensagem, int minimo, int maximo) {
        while (true) {
            int valor = lerInteiro(mensagem);
            if (valor >= minimo && valor <= maximo) return valor;
            System.out.println("Digite um valor entre " + minimo + " e " + maximo + ".");
        }
    }
}
