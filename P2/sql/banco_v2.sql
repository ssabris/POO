-- =============================================================================
-- Sistema de Biblioteca — Script v2
-- ALTERAÇÕES em relação ao v1:
--   1. Tabela `copia` eliminada; `obra` recebe codigo_barras, qtd_total e qtd_disponivel
--   2. `funcionario` ganha `matricula_func` UNIQUE (no lugar de cpf)
--   3. `emprestimo` tem 3 datas:
--        data_emprestimo         — dia em que o livro saiu (hoje)
--        data_prevista_devolucao — prazo combinado (ex: hoje + 14 dias)
--        data_devolucao_real     — quando o leitor efetivamente devolveu (NULL = em aberto)
--   4. FK em `emprestimo` aponta para `obra` (antes apontava para `copia`)
--   5. Coluna `ativo` em `leitor` e `funcionario` para Soft Delete
--
-- NOTA: usa o banco `biblioteca_v2` para não conflitar com instalações anteriores.
--       O banco antigo (`biblioteca`) não é tocado.
-- =============================================================================

CREATE DATABASE IF NOT EXISTS biblioteca_v2
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE biblioteca_v2;

-- ─────────────────────────────────────────────
-- Funcionários
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS funcionario (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    nome           VARCHAR(100) NOT NULL,
    cargo          VARCHAR(80)  NOT NULL,
    matricula_func VARCHAR(30)  NOT NULL UNIQUE,
    ativo          BOOLEAN      NOT NULL DEFAULT TRUE
);

-- ─────────────────────────────────────────────
-- Leitores
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS leitor (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    matricula VARCHAR(30)  NOT NULL UNIQUE,
    ativo     BOOLEAN      NOT NULL DEFAULT TRUE
);

-- ─────────────────────────────────────────────
-- Obras  (unificada com Cópias)
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS obra (
    id                    INT AUTO_INCREMENT PRIMARY KEY,
    titulo                VARCHAR(200) NOT NULL,
    autor                 VARCHAR(100) NOT NULL,
    codigo_barras         VARCHAR(50)  NOT NULL UNIQUE,
    quantidade_total      INT          NOT NULL DEFAULT 1,
    quantidade_disponivel INT          NOT NULL DEFAULT 1
);

-- ─────────────────────────────────────────────
-- Empréstimos  (3 datas; FK aponta para obra)
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS emprestimo (
    id                      INT  AUTO_INCREMENT PRIMARY KEY,
    id_leitor               INT  NOT NULL,
    id_obra                 INT  NOT NULL,
    id_funcionario          INT  NOT NULL,
    data_emprestimo         DATE NOT NULL,
    data_prevista_devolucao DATE NOT NULL,
    data_devolucao_real     DATE NULL,
    CONSTRAINT fk_emp_leitor FOREIGN KEY (id_leitor)      REFERENCES leitor(id),
    CONSTRAINT fk_emp_obra   FOREIGN KEY (id_obra)        REFERENCES obra(id),
    CONSTRAINT fk_emp_func   FOREIGN KEY (id_funcionario) REFERENCES funcionario(id)
);

-- ─────────────────────────────────────────────
-- Reservas
-- ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS reserva (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    id_leitor    INT  NOT NULL,
    id_obra      INT  NOT NULL,
    data_reserva DATE NOT NULL,
    CONSTRAINT fk_res_leitor FOREIGN KEY (id_leitor) REFERENCES leitor(id),
    CONSTRAINT fk_res_obra   FOREIGN KEY (id_obra)   REFERENCES obra(id)
);

-- ─────────────────────────────────────────────
-- Dados de exemplo
-- ─────────────────────────────────────────────
INSERT INTO funcionario (nome, cargo, matricula_func) VALUES
    ('Carlos Souza', 'Bibliotecário', 'FUNC-001'),
    ('Ana Lima',     'Atendente',     'FUNC-002');

INSERT INTO leitor (nome, matricula) VALUES
    ('João Silva',     'MAT-001'),
    ('Maria Oliveira', 'MAT-002');

INSERT INTO obra (titulo, autor, codigo_barras, quantidade_total, quantidade_disponivel) VALUES
    ('Dom Casmurro',      'Machado de Assis', 'BC-0001', 2, 2),
    ('O Cortiço',         'Aluísio Azevedo',  'BC-0002', 1, 1),
    ('Capitães da Areia', 'Jorge Amado',      'BC-0003', 3, 3);
