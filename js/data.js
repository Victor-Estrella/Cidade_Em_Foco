/* Dados demonstrativos do domínio Cidade em Foco.
   Array de objetos utilizado nas páginas e nos filtros do protótipo. */
const categorias = [
  { id: 1, nome: "Infraestrutura viária", descricao: "Ruas, buracos e sinalização" },
  { id: 2, nome: "Iluminação pública", descricao: "Postes e iluminação" },
  { id: 3, nome: "Limpeza urbana", descricao: "Coleta, descarte e limpeza" },
  { id: 4, nome: "Acessibilidade", descricao: "Calçadas e mobilidade" },
  { id: 5, nome: "Espaços públicos", descricao: "Praças e áreas de convivência" }
];

const ocorrenciasIniciais = [
  { id: 1, titulo: "Poste de iluminação apagado", descricao: "O poste está apagado há vários dias, deixando o trecho da rua muito escuro durante a noite.", endereco: "Rua das Flores, próximo ao nº 120", bairro: "Vila Maria", categoriaId: 2, gravidade: 2, pessoasAfetadas: 35, status: "EM_ANALISE", dataCadastro: "2026-11-12", confirmacoes: 8, responsavel: "Mariana Costa" },
  { id: 2, titulo: "Buraco na faixa da direita", descricao: "Um buraco grande se formou próximo ao cruzamento e os veículos precisam desviar.", endereco: "Av. Guilherme Cotching, 450", bairro: "Vila Maria", categoriaId: 1, gravidade: 3, pessoasAfetadas: 80, status: "ABERTA", dataCadastro: "2026-11-11", confirmacoes: 14, responsavel: "Rafael Lima" },
  { id: 3, titulo: "Calçada com acessibilidade comprometida", descricao: "A calçada está quebrada e dificulta a passagem de cadeirantes e pessoas com carrinho de bebê.", endereco: "Rua Serra de Bragança, 80", bairro: "Tatuapé", categoriaId: 4, gravidade: 2, pessoasAfetadas: 24, status: "EM_ATENDIMENTO", dataCadastro: "2026-11-10", confirmacoes: 6, responsavel: "Ana Souza" },
  { id: 4, titulo: "Descarte irregular de entulho", descricao: "Há sacos de entulho e móveis descartados na calçada, ocupando parte da passagem.", endereco: "Rua Itapura, esquina com Rua Azevedo", bairro: "Tatuapé", categoriaId: 3, gravidade: 2, pessoasAfetadas: 18, status: "ABERTA", dataCadastro: "2026-11-09", confirmacoes: 4, responsavel: "João Pedro" },
  { id: 5, titulo: "Bancos da praça danificados", descricao: "Dois bancos estão quebrados e há peças soltas na área de convivência.", endereco: "Praça da Integração", bairro: "Carrão", categoriaId: 5, gravidade: 1, pessoasAfetadas: 12, status: "RESOLVIDA", dataCadastro: "2026-11-06", confirmacoes: 3, responsavel: "Camila Rocha" },
  { id: 6, titulo: "Semáforo com sinal intermitente", descricao: "O semáforo está piscando de forma irregular no horário de maior movimento.", endereco: "Av. Conselheiro Carrão, 900", bairro: "Carrão", categoriaId: 1, gravidade: 3, pessoasAfetadas: 120, status: "EM_ANALISE", dataCadastro: "2026-11-05", confirmacoes: 21, responsavel: "Lucas Martins" },
  { id: 7, titulo: "Lixeira pública transbordando", descricao: "A lixeira está cheia há dias e resíduos estão espalhados pela calçada.", endereco: "Rua Tuiuti, 310", bairro: "Tatuapé", categoriaId: 3, gravidade: 1, pessoasAfetadas: 15, status: "RESOLVIDA", dataCadastro: "2026-11-03", confirmacoes: 2, responsavel: "Beatriz Alves" }
];

function carregarOcorrencias() {
  const salvas = localStorage.getItem("cidadeEmFocoOcorrencias");
  if (salvas) {
    try {
      return JSON.parse(salvas);
    } catch (erro) {
      console.warn("Não foi possível ler os registros locais.", erro);
    }
  }
  return ocorrenciasIniciais.slice();
}

function salvarOcorrencias(lista) {
  localStorage.setItem("cidadeEmFocoOcorrencias", JSON.stringify(lista));
}
