# Cidade em Foco

Site onde o cidadão pode registrar problemas do seu bairro (buraco na rua, poste apagado, lixo acumulado...) e acompanhar as ocorrências já cadastradas. Feito para a Global Solution da FIAP, só com HTML, CSS e JavaScript.

## Como rodar

Clone o repositório e abra o `index.html` no navegador. Não precisa instalar nada.

## Páginas

- **Início** (`index.html`): apresentação, contadores e as ocorrências mais recentes.
- **Ocorrências** (`ocorrencias.html`): lista com filtro por texto, categoria, status e bairro.
- **Registrar** (`registrar.html`): formulário para cadastrar uma nova ocorrência, com validação.

## Como funciona

As ocorrências de exemplo ficam em `js/data.js`. Quando alguém cadastra uma nova, ela é salva no `localStorage` do navegador e passa a aparecer na lista e nos contadores da página inicial. Os dados não saem do navegador, não existe servidor nem banco de dados.

## Estrutura

```
index.html
ocorrencias.html
registrar.html
css/style.css
js/data.js      dados e localStorage
js/app.js       menu, filtros, cards e validação
img/            imagem da página inicial
```
