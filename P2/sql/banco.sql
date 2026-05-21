-- Script de criação do banco de dados para o Sistema de Biblioteca
-- Execute no MySQL antes de rodar a aplicação

CREATE DATABASE IF NOT EXISTS biblioteca
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE biblioteca;

-- Tabela de Funcionários
CREATE TABLE IF NOT EXISTS funcionario (
    id    INT AUTO_INCREMENT PRIMARY KEY,
    nome  VARCHAR(100) NOT NULL,
    cargo VARCHAR(80)  NOT NULL
);

-- Tabela de Leitores
CREATE TABLE IF NOT EXISTS leitor (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    matricula VARCHAR(30)  NOT NULL UNIQUE
);

-- Tabela de Obras
CREATE TABLE IF NOT EXISTS obra (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    autor  VARCHAR(100) NOT NULL
);

-- Tabela de Cópias (AGREGAÇÃO: cada cópia pertence a uma obra)
CREATE TABLE IF NOT EXISTS copia (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    id_obra       INT NOT NULL,
    codigo_barras VARCHAR(50) NOT NULL UNIQUE,
    CONSTRAINT fk_copia_obra FOREIGN KEY (id_obra) REFERENCES obra(id)
);

-- Tabela de Empréstimos (ASSOCIAÇÃO: relaciona leitor, cópia e funcionário)
CREATE TABLE IF NOT EXISTS emprestimo (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    id_leitor       INT  NOT NULL,
    id_copia        INT  NOT NULL,
    id_funcionario  INT  NOT NULL,
    data_emprestimo DATE NOT NULL,
    data_devolucao  DATE NULL,         -- NULL = ainda não devolvido
    CONSTRAINT fk_emp_leitor  FOREIGN KEY (id_leitor)      REFERENCES leitor(id),
    CONSTRAINT fk_emp_copia   FOREIGN KEY (id_copia)       REFERENCES copia(id),
    CONSTRAINT fk_emp_func    FOREIGN KEY (id_funcionario)  REFERENCES funcionario(id)
);

-- Tabela de Reservas (ASSOCIAÇÃO: leitor reserva uma obra)
CREATE TABLE IF NOT EXISTS reserva (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    id_leitor    INT  NOT NULL,
    id_obra      INT  NOT NULL,
    data_reserva DATE NOT NULL,
    CONSTRAINT fk_res_leitor FOREIGN KEY (id_leitor) REFERENCES leitor(id),
    CONSTRAINT fk_res_obra   FOREIGN KEY (id_obra)   REFERENCES obra(id)
);

-- Dados de exemplo para facilitar os testes
INSERT INTO funcionario (nome, cargo) VALUES
    ('Carlos Souza', 'Bibliotecário'),
    ('Ana Lima', 'Atendente');

INSERT INTO leitor (nome, matricula) VALUES
    ('João Silva', 'MAT-001'),
    ('Maria Oliveira', 'MAT-002');

INSERT INTO obra (titulo, autor) VALUES
    ('Dom Casmurro', 'Machado de Assis'),
    ('O Cortiço', 'Aluísio Azevedo');

INSERT INTO copia (id_obra, codigo_barras) VALUES
    (1, 'BC-0001'),
    (1, 'BC-0002'),
    (2, 'BC-0003');
