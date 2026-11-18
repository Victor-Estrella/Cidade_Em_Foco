/*
  CIDADE EM FOCO
  Global Solution 2026/2 - FIAP
  Banco de dados: Oracle Database (SQL Developer)

  Modelo: 5 tabelas
  - USUARIO
  - CATEGORIA
  - OCORRENCIA
  - CONFIRMACAO
  - ATUALIZACAO

  Observacao:
  Este script cria as tabelas e insere dados demonstrativos.
  A pontuacao de prioridade e calculada nas consultas, considerando
  gravidade, pessoas afetadas e tempo desde o registro.
*/

-- =========================================================
-- 1. LIMPEZA (opcional)
-- Execute somente se quiser apagar uma instalacao anterior.
-- Descomente as linhas abaixo na ordem apresentada.
-- =========================================================
-- DROP TABLE ATUALIZACAO CASCADE CONSTRAINTS;
-- DROP TABLE CONFIRMACAO CASCADE CONSTRAINTS;
-- DROP TABLE OCORRENCIA CASCADE CONSTRAINTS;
-- DROP TABLE CATEGORIA CASCADE CONSTRAINTS;
-- DROP TABLE USUARIO CASCADE CONSTRAINTS;

-- =========================================================
-- 2. TABELA: USUARIO
-- Armazena cidadaos e responsaveis pela gestao das ocorrencias.
-- =========================================================
CREATE TABLE USUARIO (
    ID_USUARIO          NUMBER(6)       NOT NULL,
    NOME                VARCHAR2(100)   NOT NULL,
    EMAIL               VARCHAR2(120)   NOT NULL,
    SENHA_HASH          VARCHAR2(255)   NOT NULL,
    BAIRRO              VARCHAR2(80),
    TIPO_USUARIO        VARCHAR2(15)    DEFAULT 'CIDADAO' NOT NULL,

    CONSTRAINT PK_USUARIO PRIMARY KEY (ID_USUARIO),
    CONSTRAINT UK_USUARIO_EMAIL UNIQUE (EMAIL),
    CONSTRAINT CK_USUARIO_TIPO
        CHECK (TIPO_USUARIO IN ('CIDADAO', 'RESPONSAVEL'))
);

-- =========================================================
-- 3. TABELA: CATEGORIA
-- Categorias disponiveis para classificacao das ocorrencias.
-- =========================================================
CREATE TABLE CATEGORIA (
    ID_CATEGORIA        NUMBER(4)       NOT NULL,
    NOME                VARCHAR2(60)    NOT NULL,
    DESCRICAO           VARCHAR2(250),

    CONSTRAINT PK_CATEGORIA PRIMARY KEY (ID_CATEGORIA),
    CONSTRAINT UK_CATEGORIA_NOME UNIQUE (NOME)
);

-- =========================================================
-- 4. TABELA: OCORRENCIA
-- Cada registro representa um problema identificado na cidade.
-- =========================================================
CREATE TABLE OCORRENCIA (
    ID_OCORRENCIA       NUMBER(8)       NOT NULL,
    TITULO              VARCHAR2(120)   NOT NULL,
    DESCRICAO           VARCHAR2(1000)  NOT NULL,
    LOCALIZACAO         VARCHAR2(180)   NOT NULL,
    GRAVIDADE           NUMBER(1)       NOT NULL,
    PESSOAS_AFETADAS    NUMBER(8)       DEFAULT 1 NOT NULL,
    DATA_REGISTRO       DATE            DEFAULT SYSDATE NOT NULL,
    STATUS              VARCHAR2(20)    DEFAULT 'ABERTA' NOT NULL,
    ID_USUARIO          NUMBER(6)       NOT NULL,
    ID_CATEGORIA        NUMBER(4)       NOT NULL,

    CONSTRAINT PK_OCORRENCIA PRIMARY KEY (ID_OCORRENCIA),
    CONSTRAINT FK_OCORRENCIA_USUARIO
        FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO (ID_USUARIO),
    CONSTRAINT FK_OCORRENCIA_CATEGORIA
        FOREIGN KEY (ID_CATEGORIA) REFERENCES CATEGORIA (ID_CATEGORIA),
    CONSTRAINT CK_OCORRENCIA_GRAVIDADE
        CHECK (GRAVIDADE IN (1, 2, 3)),
    CONSTRAINT CK_OCORRENCIA_PESSOAS
        CHECK (PESSOAS_AFETADAS >= 1),
    CONSTRAINT CK_OCORRENCIA_STATUS
        CHECK (STATUS IN ('ABERTA', 'EM_ANALISE', 'EM_ATENDIMENTO', 'RESOLVIDA'))
);

-- =========================================================
-- 5. TABELA: CONFIRMACAO
-- Registra cidadaos que confirmaram uma ocorrencia.
-- Um usuario so pode confirmar a mesma ocorrencia uma vez.
-- =========================================================
CREATE TABLE CONFIRMACAO (
    ID_CONFIRMACAO      NUMBER(8)       NOT NULL,
    ID_USUARIO          NUMBER(6)       NOT NULL,
    ID_OCORRENCIA       NUMBER(8)       NOT NULL,
    DATA_CONFIRMACAO    DATE            DEFAULT SYSDATE NOT NULL,

    CONSTRAINT PK_CONFIRMACAO PRIMARY KEY (ID_CONFIRMACAO),
    CONSTRAINT FK_CONFIRMACAO_USUARIO
        FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO (ID_USUARIO),
    CONSTRAINT FK_CONFIRMACAO_OCORRENCIA
        FOREIGN KEY (ID_OCORRENCIA) REFERENCES OCORRENCIA (ID_OCORRENCIA),
    CONSTRAINT UK_CONFIRMACAO_USUARIO_OCORRENCIA
        UNIQUE (ID_USUARIO, ID_OCORRENCIA)
);

-- =========================================================
-- 6. TABELA: ATUALIZACAO
-- Guarda o historico de alteracoes de status das ocorrencias.
-- O responsavel e um usuario do tipo RESPONSAVEL (regra validada
-- pela aplicacao Java; a FK garante que o usuario exista).
-- =========================================================
CREATE TABLE ATUALIZACAO (
    ID_ATUALIZACAO      NUMBER(8)       NOT NULL,
    ID_OCORRENCIA       NUMBER(8)       NOT NULL,
    ID_USUARIO          NUMBER(6)       NOT NULL,
    STATUS_ANTERIOR     VARCHAR2(20),
    NOVO_STATUS         VARCHAR2(20)    NOT NULL,
    DESCRICAO           VARCHAR2(500)   NOT NULL,
    DATA_ATUALIZACAO    DATE            DEFAULT SYSDATE NOT NULL,

    CONSTRAINT PK_ATUALIZACAO PRIMARY KEY (ID_ATUALIZACAO),
    CONSTRAINT FK_ATUALIZACAO_OCORRENCIA
        FOREIGN KEY (ID_OCORRENCIA) REFERENCES OCORRENCIA (ID_OCORRENCIA),
    CONSTRAINT FK_ATUALIZACAO_USUARIO
        FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO (ID_USUARIO),
    CONSTRAINT CK_ATUALIZACAO_STATUS_ANT
        CHECK (STATUS_ANTERIOR IS NULL OR STATUS_ANTERIOR IN
            ('ABERTA', 'EM_ANALISE', 'EM_ATENDIMENTO', 'RESOLVIDA')),
    CONSTRAINT CK_ATUALIZACAO_STATUS_NOVO
        CHECK (NOVO_STATUS IN
            ('ABERTA', 'EM_ANALISE', 'EM_ATENDIMENTO', 'RESOLVIDA'))
);

-- =========================================================
-- 7. DADOS DE EXEMPLO
-- Os usuarios e enderecos sao ficticios.
-- SENHA_HASH contem valores demonstrativos, nao senhas reais.
-- =========================================================

INSERT INTO USUARIO
    (ID_USUARIO, NOME, EMAIL, SENHA_HASH, BAIRRO, TIPO_USUARIO)
VALUES
    (1, 'Ana Souza', 'ana@email.com', 'hash_demo_ana', 'Tatuape', 'CIDADAO');

INSERT INTO USUARIO
    (ID_USUARIO, NOME, EMAIL, SENHA_HASH, BAIRRO, TIPO_USUARIO)
VALUES
    (2, 'Carlos Lima', 'carlos@email.com', 'hash_demo_carlos', 'Mooca', 'CIDADAO');

INSERT INTO USUARIO
    (ID_USUARIO, NOME, EMAIL, SENHA_HASH, BAIRRO, TIPO_USUARIO)
VALUES
    (3, 'Mariana Alves', 'mariana@email.com', 'hash_demo_mariana', 'Carrão', 'CIDADAO');

INSERT INTO USUARIO
    (ID_USUARIO, NOME, EMAIL, SENHA_HASH, BAIRRO, TIPO_USUARIO)
VALUES
    (4, 'Equipe de Zeladoria', 'zeladoria@cidadeemfoco.com', 'hash_demo_responsavel', 'Centro', 'RESPONSAVEL');

INSERT INTO CATEGORIA (ID_CATEGORIA, NOME, DESCRICAO)
VALUES (1, 'Infraestrutura viaria', 'Buracos, pavimentacao danificada e problemas nas vias.');

INSERT INTO CATEGORIA (ID_CATEGORIA, NOME, DESCRICAO)
VALUES (2, 'Iluminacao publica', 'Postes, lampadas e pontos de iluminacao danificados.');

INSERT INTO CATEGORIA (ID_CATEGORIA, NOME, DESCRICAO)
VALUES (3, 'Limpeza urbana', 'Descarte irregular, lixo acumulado e entulho.');

INSERT INTO CATEGORIA (ID_CATEGORIA, NOME, DESCRICAO)
VALUES (4, 'Acessibilidade', 'Calcadas, rampas e barreiras de acessibilidade.');

INSERT INTO CATEGORIA (ID_CATEGORIA, NOME, DESCRICAO)
VALUES (5, 'Espacos publicos', 'Pracas, parques e areas de convivencia.');

INSERT INTO OCORRENCIA
    (ID_OCORRENCIA, TITULO, DESCRICAO, LOCALIZACAO, GRAVIDADE,
     PESSOAS_AFETADAS, DATA_REGISTRO, STATUS, ID_USUARIO, ID_CATEGORIA)
VALUES
    (1, 'Buraco em avenida', 'Buraco grande prejudicando o trafego de veiculos.',
     'Avenida Central, Tatuape', 3, 45, SYSDATE - 12, 'EM_ANALISE', 1, 1);

INSERT INTO OCORRENCIA
    (ID_OCORRENCIA, TITULO, DESCRICAO, LOCALIZACAO, GRAVIDADE,
     PESSOAS_AFETADAS, DATA_REGISTRO, STATUS, ID_USUARIO, ID_CATEGORIA)
VALUES
    (2, 'Poste com lampada apagada', 'Trecho da rua permanece sem iluminacao durante a noite.',
     'Rua das Flores, Carrão', 2, 12, SYSDATE - 4, 'ABERTA', 3, 2);

INSERT INTO OCORRENCIA
    (ID_OCORRENCIA, TITULO, DESCRICAO, LOCALIZACAO, GRAVIDADE,
     PESSOAS_AFETADAS, DATA_REGISTRO, STATUS, ID_USUARIO, ID_CATEGORIA)
VALUES
    (3, 'Acumulo de lixo na calcada', 'Descarte irregular de residuos proximo a area residencial.',
     'Rua do Mercado, Mooca', 3, 30, SYSDATE - 35, 'EM_ATENDIMENTO', 2, 3);

INSERT INTO OCORRENCIA
    (ID_OCORRENCIA, TITULO, DESCRICAO, LOCALIZACAO, GRAVIDADE,
     PESSOAS_AFETADAS, DATA_REGISTRO, STATUS, ID_USUARIO, ID_CATEGORIA)
VALUES
    (4, 'Calcada danificada', 'Piso irregular dificulta a passagem de pedestres.',
     'Rua Primavera, Tatuape', 2, 8, SYSDATE - 2, 'ABERTA', 1, 4);

INSERT INTO OCORRENCIA
    (ID_OCORRENCIA, TITULO, DESCRICAO, LOCALIZACAO, GRAVIDADE,
     PESSOAS_AFETADAS, DATA_REGISTRO, STATUS, ID_USUARIO, ID_CATEGORIA)
VALUES
    (5, 'Praca com equipamentos danificados', 'Equipamentos de uso coletivo precisam de manutencao.',
     'Praca do Bairro, Carrão', 1, 6, SYSDATE - 18, 'RESOLVIDA', 3, 5);

INSERT INTO CONFIRMACAO
    (ID_CONFIRMACAO, ID_USUARIO, ID_OCORRENCIA, DATA_CONFIRMACAO)
VALUES (1, 2, 1, SYSDATE - 10);

INSERT INTO CONFIRMACAO
    (ID_CONFIRMACAO, ID_USUARIO, ID_OCORRENCIA, DATA_CONFIRMACAO)
VALUES (2, 3, 1, SYSDATE - 9);

INSERT INTO CONFIRMACAO
    (ID_CONFIRMACAO, ID_USUARIO, ID_OCORRENCIA, DATA_CONFIRMACAO)
VALUES (3, 1, 2, SYSDATE - 3);

INSERT INTO CONFIRMACAO
    (ID_CONFIRMACAO, ID_USUARIO, ID_OCORRENCIA, DATA_CONFIRMACAO)
VALUES (4, 1, 3, SYSDATE - 30);

INSERT INTO CONFIRMACAO
    (ID_CONFIRMACAO, ID_USUARIO, ID_OCORRENCIA, DATA_CONFIRMACAO)
VALUES (5, 2, 3, SYSDATE - 29);

INSERT INTO ATUALIZACAO
    (ID_ATUALIZACAO, ID_OCORRENCIA, ID_USUARIO, STATUS_ANTERIOR,
     NOVO_STATUS, DESCRICAO, DATA_ATUALIZACAO)
VALUES
    (1, 1, 4, 'ABERTA', 'EM_ANALISE',
     'Ocorrencia recebida e encaminhada para avaliacao.', SYSDATE - 8);

INSERT INTO ATUALIZACAO
    (ID_ATUALIZACAO, ID_OCORRENCIA, ID_USUARIO, STATUS_ANTERIOR,
     NOVO_STATUS, DESCRICAO, DATA_ATUALIZACAO)
VALUES
    (2, 3, 4, 'ABERTA', 'EM_ATENDIMENTO',
     'Servico de limpeza programado para o local.', SYSDATE - 20);

INSERT INTO ATUALIZACAO
    (ID_ATUALIZACAO, ID_OCORRENCIA, ID_USUARIO, STATUS_ANTERIOR,
     NOVO_STATUS, DESCRICAO, DATA_ATUALIZACAO)
VALUES
    (3, 5, 4, 'EM_ATENDIMENTO', 'RESOLVIDA',
     'Manutencao dos equipamentos concluida.', SYSDATE - 5);

COMMIT;

-- =========================================================
-- 8. CONSULTAS DE EXEMPLO (SELECT + JOIN)
-- =========================================================

-- 8.1. Listar ocorrencias com categoria e autor do registro.
SELECT
    O.ID_OCORRENCIA,
    O.TITULO,
    C.NOME AS CATEGORIA,
    U.NOME AS AUTOR,
    O.LOCALIZACAO,
    O.STATUS,
    O.DATA_REGISTRO
FROM OCORRENCIA O
INNER JOIN CATEGORIA C
    ON O.ID_CATEGORIA = C.ID_CATEGORIA
INNER JOIN USUARIO U
    ON O.ID_USUARIO = U.ID_USUARIO
ORDER BY O.DATA_REGISTRO DESC;

-- 8.2. Exibir ocorrencias com quantidade de confirmacoes.
SELECT
    O.ID_OCORRENCIA,
    O.TITULO,
    COUNT(CF.ID_CONFIRMACAO) AS TOTAL_CONFIRMACOES
FROM OCORRENCIA O
LEFT JOIN CONFIRMACAO CF
    ON O.ID_OCORRENCIA = CF.ID_OCORRENCIA
GROUP BY O.ID_OCORRENCIA, O.TITULO
ORDER BY TOTAL_CONFIRMACOES DESC;

-- 8.3. Calcular prioridade conforme a regra inicial do projeto.
-- Gravidade: 1 a 3 pontos.
-- Pessoas afetadas: 1 a 4 pontos.
-- Tempo: 1 a 3 pontos.
SELECT
    O.ID_OCORRENCIA,
    O.TITULO,
    O.GRAVIDADE,
    O.PESSOAS_AFETADAS,
    TRUNC(SYSDATE - O.DATA_REGISTRO) AS DIAS_ABERTA,
    (
        O.GRAVIDADE
        + CASE
            WHEN O.PESSOAS_AFETADAS <= 5 THEN 1
            WHEN O.PESSOAS_AFETADAS <= 20 THEN 2
            WHEN O.PESSOAS_AFETADAS <= 50 THEN 3
            ELSE 4
          END
        + CASE
            WHEN TRUNC(SYSDATE - O.DATA_REGISTRO) <= 7 THEN 1
            WHEN TRUNC(SYSDATE - O.DATA_REGISTRO) <= 30 THEN 2
            ELSE 3
          END
    ) AS PONTUACAO_PRIORIDADE,
    CASE
        WHEN (
            O.GRAVIDADE
            + CASE
                WHEN O.PESSOAS_AFETADAS <= 5 THEN 1
                WHEN O.PESSOAS_AFETADAS <= 20 THEN 2
                WHEN O.PESSOAS_AFETADAS <= 50 THEN 3
                ELSE 4
              END
            + CASE
                WHEN TRUNC(SYSDATE - O.DATA_REGISTRO) <= 7 THEN 1
                WHEN TRUNC(SYSDATE - O.DATA_REGISTRO) <= 30 THEN 2
                ELSE 3
              END
        ) >= 7 THEN 'ALTA'
        WHEN (
            O.GRAVIDADE
            + CASE
                WHEN O.PESSOAS_AFETADAS <= 5 THEN 1
                WHEN O.PESSOAS_AFETADAS <= 20 THEN 2
                WHEN O.PESSOAS_AFETADAS <= 50 THEN 3
                ELSE 4
              END
            + CASE
                WHEN TRUNC(SYSDATE - O.DATA_REGISTRO) <= 7 THEN 1
                WHEN TRUNC(SYSDATE - O.DATA_REGISTRO) <= 30 THEN 2
                ELSE 3
              END
        ) >= 4 THEN 'MEDIA'
        ELSE 'BAIXA'
    END AS NIVEL_PRIORIDADE
FROM OCORRENCIA O
WHERE O.STATUS <> 'RESOLVIDA'
ORDER BY PONTUACAO_PRIORIDADE DESC;

-- 8.4. Consultar o historico de uma ocorrencia.
SELECT
    O.TITULO,
    A.STATUS_ANTERIOR,
    A.NOVO_STATUS,
    A.DESCRICAO,
    U.NOME AS RESPONSAVEL,
    A.DATA_ATUALIZACAO
FROM ATUALIZACAO A
INNER JOIN OCORRENCIA O
    ON A.ID_OCORRENCIA = O.ID_OCORRENCIA
INNER JOIN USUARIO U
    ON A.ID_USUARIO = U.ID_USUARIO
WHERE O.ID_OCORRENCIA = 1
ORDER BY A.DATA_ATUALIZACAO;

-- =========================================================
-- FIM DO SCRIPT
-- =========================================================
