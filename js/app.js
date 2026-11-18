document.addEventListener("DOMContentLoaded", function () {
  const pagina = document.body.dataset.page;
  configurarMenu();

  if (pagina === "home") carregarInicio();
  if (pagina === "occurrences") configurarOcorrencias();
  if (pagina === "register") configurarFormulario();
});

function configurarMenu() {
  const botao = document.querySelector(".menu-toggle");
  const menu = document.querySelector(".main-nav");
  if (!botao || !menu) return;

  botao.addEventListener("click", function () {
    const aberto = menu.classList.toggle("menu-open");
    botao.setAttribute("aria-expanded", String(aberto));
    botao.setAttribute("aria-label", aberto ? "Fechar menu" : "Abrir menu");
  });
}

function nomeCategoria(id) {
  const categoria = categorias.find(item => item.id === Number(id));
  return categoria ? categoria.nome : "Sem categoria";
}

function nomeStatus(status) {
  const nomes = {
    ABERTA: "Aberta",
    EM_ANALISE: "Em análise",
    EM_ATENDIMENTO: "Em atendimento",
    RESOLVIDA: "Resolvida"
  };
  return nomes[status] || status;
}

function classeStatus(status) {
  return (status || "").toLowerCase().replaceAll("_", "-");
}

function formatarData(data) {
  if (!data) return "Data não informada";
  const partes = data.split("-");
  return partes.length === 3 ? `${partes[2]}/${partes[1]}/${partes[0]}` : data;
}

function criarCardOcorrencia(item, compacto) {
  const card = document.createElement("article");
  card.className = compacto ? "occurrence-card compact-card" : "occurrence-row";

  const topo = document.createElement("div");
  topo.className = "occurrence-card-top";
  const categoria = document.createElement("span");
  categoria.className = "category-tag";
  categoria.textContent = nomeCategoria(item.categoriaId);
  const status = document.createElement("span");
  status.className = `status-tag status-${classeStatus(item.status)}`;
  status.textContent = nomeStatus(item.status);
  topo.append(categoria, status);

  const titulo = document.createElement("h3");
  titulo.textContent = item.titulo;
  const descricao = document.createElement("p");
  descricao.className = "occurrence-description";
  descricao.textContent = item.descricao;

  const meta = document.createElement("div");
  meta.className = "occurrence-meta";
  const local = document.createElement("span");
  local.textContent = `⌖ ${item.bairro}`;
  const data = document.createElement("span");
  data.textContent = `◷ ${formatarData(item.dataCadastro)}`;
  const confirmacoes = document.createElement("span");
  confirmacoes.textContent = `♡ ${item.confirmacoes || 0} confirmações`;
  meta.append(local, data, confirmacoes);

  card.append(topo, titulo, descricao, meta);
  return card;
}

function carregarInicio() {
  const lista = carregarOcorrencias();
  const abertas = lista.filter(item => item.status !== "RESOLVIDA").length;
  const resolvidas = lista.filter(item => item.status === "RESOLVIDA").length;

  document.getElementById("stat-total").textContent = lista.length;
  document.getElementById("stat-open").textContent = abertas;
  document.getElementById("stat-resolved").textContent = resolvidas;

  const destino = document.getElementById("home-occurrences");
  lista.slice().sort((a, b) => b.dataCadastro.localeCompare(a.dataCadastro)).slice(0, 3)
    .forEach(item => destino.appendChild(criarCardOcorrencia(item, true)));
}

function configurarOcorrencias() {
  const busca = document.getElementById("search-occurrence");
  const categoria = document.getElementById("filter-category");
  const status = document.getElementById("filter-status");
  const bairro = document.getElementById("filter-neighborhood");
  const limpar = document.getElementById("clear-filters");
  const lista = document.getElementById("occurrence-list");

  categorias.forEach(item => {
    const opcao = document.createElement("option");
    opcao.value = item.id;
    opcao.textContent = item.nome;
    categoria.appendChild(opcao);
  });

  function renderizar() {
    const termo = busca.value.trim().toLocaleLowerCase("pt-BR");
    const categoriaSelecionada = categoria.value;
    const statusSelecionado = status.value;
    const bairroDigitado = bairro.value.trim().toLocaleLowerCase("pt-BR");

    const filtradas = carregarOcorrencias().filter(item => {
      const texto = `${item.titulo} ${item.descricao} ${item.endereco} ${item.bairro}`.toLocaleLowerCase("pt-BR");
      return (!termo || texto.includes(termo))
        && (!categoriaSelecionada || String(item.categoriaId) === categoriaSelecionada)
        && (!statusSelecionado || item.status === statusSelecionado)
        && (!bairroDigitado || item.bairro.toLocaleLowerCase("pt-BR").includes(bairroDigitado));
    }).sort((a, b) => b.dataCadastro.localeCompare(a.dataCadastro));

    lista.replaceChildren();
    filtradas.forEach(item => lista.appendChild(criarCardOcorrencia(item, false)));
    document.getElementById("results-count").textContent = `${filtradas.length} ${filtradas.length === 1 ? "ocorrência" : "ocorrências"}`;
    document.getElementById("empty-state").hidden = filtradas.length !== 0;
  }

  [busca, categoria, status, bairro].forEach(campo => {
    campo.addEventListener(campo.tagName === "SELECT" ? "change" : "input", renderizar);
  });
  limpar.addEventListener("click", function () {
    busca.value = "";
    categoria.value = "";
    status.value = "";
    bairro.value = "";
    renderizar();
  });
  renderizar();
}

function configurarFormulario() {
  const form = document.getElementById("occurrence-form");
  const selectCategoria = document.getElementById("category");
  const descricao = document.getElementById("description");
  const contador = document.getElementById("description-counter");
  const feedback = document.getElementById("form-feedback");

  categorias.forEach(item => {
    const opcao = document.createElement("option");
    opcao.value = item.id;
    opcao.textContent = item.nome;
    selectCategoria.appendChild(opcao);
  });

  descricao.addEventListener("input", () => {
    contador.textContent = `${descricao.value.length}/500`;
  });

  const campos = ["citizen-name", "citizen-email", "title", "description", "category", "severity", "address", "neighborhood", "people"];

  function mostrarErro(id, mensagem) {
    const campo = document.getElementById(id);
    const erro = document.getElementById(`error-${id}`);
    campo.setAttribute("aria-invalid", mensagem ? "true" : "false");
    erro.textContent = mensagem;
  }

  function validar() {
    let valido = true;
    campos.forEach(id => mostrarErro(id, ""));

    const nome = document.getElementById("citizen-name").value.trim();
    const email = document.getElementById("citizen-email").value.trim();
    const titulo = document.getElementById("title").value.trim();
    const texto = descricao.value.trim();
    const endereco = document.getElementById("address").value.trim();
    const bairro = document.getElementById("neighborhood").value.trim();
    const pessoas = Number(document.getElementById("people").value);

    const regras = [
      ["citizen-name", nome.length >= 3, "Informe seu nome (mínimo de 3 caracteres)."],
      ["citizen-email", /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email), "Informe um e-mail válido."],
      ["title", titulo.length >= 5, "O título deve ter pelo menos 5 caracteres."],
      ["description", texto.length >= 15, "Descreva o problema com pelo menos 15 caracteres."],
      ["category", Boolean(selectCategoria.value), "Selecione uma categoria."],
      ["severity", Boolean(document.getElementById("severity").value), "Selecione a gravidade."],
      ["address", endereco.length >= 5, "Informe um endereço válido."],
      ["neighborhood", bairro.length >= 2, "Informe o bairro."],
      ["people", Number.isInteger(pessoas) && pessoas >= 1 && pessoas <= 100000, "Informe uma quantidade entre 1 e 100.000."]
    ];

    regras.forEach(([id, regra, mensagem]) => {
      if (!regra) {
        mostrarErro(id, mensagem);
        valido = false;
      }
    });
    return valido;
  }

  campos.forEach(id => {
    const campo = document.getElementById(id);
    campo.addEventListener("input", () => {
      const erro = document.getElementById(`error-${id}`);
      if (erro.textContent) mostrarErro(id, "");
    });
    campo.addEventListener("change", () => {
      const erro = document.getElementById(`error-${id}`);
      if (erro.textContent) mostrarErro(id, "");
    });
  });

  form.addEventListener("submit", function (evento) {
    evento.preventDefault();
    feedback.hidden = true;

    if (!validar()) {
      feedback.className = "form-feedback feedback-error";
      feedback.textContent = "Revise os campos destacados antes de enviar.";
      feedback.hidden = false;
      const primeiroErro = form.querySelector('[aria-invalid="true"]');
      if (primeiroErro) primeiroErro.focus();
      return;
    }

    const registros = carregarOcorrencias();
    const novo = {
      id: registros.length ? Math.max(...registros.map(item => Number(item.id))) + 1 : 1,
      titulo: document.getElementById("title").value.trim(),
      descricao: descricao.value.trim(),
      endereco: document.getElementById("address").value.trim(),
      bairro: document.getElementById("neighborhood").value.trim(),
      categoriaId: Number(selectCategoria.value),
      gravidade: Number(document.getElementById("severity").value),
      pessoasAfetadas: Number(document.getElementById("people").value),
      status: "ABERTA",
      dataCadastro: new Date().toISOString().slice(0, 10),
      confirmacoes: 0,
      responsavel: document.getElementById("citizen-name").value.trim()
    };

    registros.unshift(novo);
    salvarOcorrencias(registros);
    feedback.className = "form-feedback feedback-success";
    feedback.textContent = `Ocorrência #${novo.id} registrada neste navegador! Você já pode encontrá-la na página de ocorrências.`;
    feedback.hidden = false;
    form.reset();
    contador.textContent = "0/500";
    campos.forEach(id => mostrarErro(id, ""));
    feedback.scrollIntoView({ behavior: "smooth", block: "center" });
  });
}
