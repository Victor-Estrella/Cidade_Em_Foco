# Cidade em Foco

Plataforma onde o cidadão registra problemas do seu bairro (buraco na rua, poste apagado, lixo acumulado...) e acompanha o andamento de cada ocorrência. Projeto da Global Solution 2026/2 da FIAP (CitizenTech).

O mesmo domínio aparece nas três partes: **ocorrência**, **categoria**, **usuário**, **confirmação** e **atualização** de status. As partes são entregues separadas, sem integração entre elas.

## Partes do projeto

| Parte | Pasta | O que tem |
|---|---|---|
| Web | raiz do repositório | protótipo em HTML, CSS e JavaScript |
| Java | `java/` | sistema de console com menu e dados em listas, mais o diagrama de classes |
| Banco | `banco/` | script `.sql` (Oracle) e diagrama entidade-relacionamento |

## Web

**Site no ar:** https://victor-estrella.github.io/Cidade_Em_Foco/

Para rodar localmente, clone o repositório e abra o `index.html` no navegador. Não precisa instalar nada.

- **Início** (`index.html`): apresentação, contadores e as ocorrências mais recentes.
- **Ocorrências** (`ocorrencias.html`): lista com filtro por texto, categoria, status e localização.
- **Registrar** (`registrar.html`): formulário para cadastrar uma ocorrência, com validação.

Os dados de exemplo ficam em `js/data.js`. Uma ocorrência nova é salva no `localStorage` do navegador e passa a aparecer na lista e nos contadores. Nada sai do navegador.

## Java

Sistema de console com cadastro, listagem, busca por ID, atualização, atualização de status com histórico, confirmação e remoção de ocorrências. Os dados ficam em listas e são perdidos ao fechar o programa. Detalhes em [`java/README.md`](java/README.md).

## Banco de dados

O arquivo `banco/cidade_em_foco.sql` cria as 5 tabelas (`USUARIO`, `CATEGORIA`, `OCORRENCIA`, `CONFIRMACAO`, `ATUALIZACAO`), insere dados de exemplo e traz consultas com `JOIN`, incluindo o cálculo de prioridade. Foi feito para o Oracle (SQL Developer). O diagrama está em `banco/der.png`.

## Estrutura

```
index.html, ocorrencias.html, registrar.html
css/style.css
js/data.js      dados e localStorage
js/app.js       menu, filtros, cards e validação
img/            imagem da página inicial
java/           projeto Java e diagrama de classes
banco/          script .sql e diagrama ER
```
