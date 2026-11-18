# Cidade em Foco: Java

Sistema de console para registrar e acompanhar ocorrências urbanas.

## Como rodar

Na pasta `java/`:

```bash
javac -encoding UTF-8 -d out src/model/*.java src/service/*.java src/Main.java
java -cp out Main
```

Ou abra a pasta `src` em uma IDE (IntelliJ, Eclipse, VS Code) e execute a classe `Main`.

## Menu

1. Cadastrar ocorrência
2. Listar ocorrências
3. Buscar por ID
4. Atualizar dados
5. Atualizar status (guarda o histórico)
6. Confirmar ocorrência
7. Remover ocorrência

## Organização

- `model/`: `Ocorrencia`, `Categoria`, `Usuario`, `Confirmacao` e `Atualizacao`.
- `service/`: `OcorrenciaService`, com as listas, as regras e as validações.
- `Main.java`: menu e leitura dos dados no console.

O diagrama de classes está em `diagrama_classes.png`. A prioridade de cada ocorrência é calculada a partir da gravidade, das pessoas afetadas e do tempo desde o registro. O usuário logado é fixo, só para testar o fluxo.
