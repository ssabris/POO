-- ============================================================
-- Script de criação do banco de dados para o Sistema Clínico
-- Execute como root (ou usuário com permissão CREATE DATABASE):
--   mysql -u root -p < setup_mysql.sql
-- ============================================================

CREATE DATABASE IF NOT EXISTS clinica
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
    
USE clinica;

-- Caso queira criar um usuário dedicado (opcional):
-- CREATE USER IF NOT EXISTS 'clinica_user'@'localhost' IDENTIFIED BY 'senha_segura';
-- GRANT ALL PRIVILEGES ON clinica.* TO 'clinica_user'@'localhost';
-- FLUSH PRIVILEGES;

-- As tabelas são criadas automaticamente pelo DAO na primeira execução,
-- mas você pode criá-las aqui manualmente se preferir:

CREATE TABLE IF NOT EXISTS medico (
    codigo        INT PRIMARY KEY AUTO_INCREMENT,
    nome          VARCHAR(100) NOT NULL,
    email         VARCHAR(100) NOT NULL,
    senha         VARCHAR(100),
    telefone      VARCHAR(20),
    documento     VARCHAR(20),
    crm           VARCHAR(20) NOT NULL,
    especialidade VARCHAR(60)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS paciente (
    codigo          INT PRIMARY KEY AUTO_INCREMENT,
    nome            VARCHAR(100) NOT NULL,
    email           VARCHAR(100) NOT NULL,
    senha           VARCHAR(100),
    telefone        VARCHAR(20),
    documento       VARCHAR(20),
    data_nascimento VARCHAR(10),
    plano_saude     VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS consulta (
    codigo          INT PRIMARY KEY AUTO_INCREMENT,
    codigo_medico   INT NOT NULL,
    codigo_paciente INT NOT NULL,
    data            VARCHAR(10) NOT NULL,
    hora            VARCHAR(5)  NOT NULL,
    status          VARCHAR(20) DEFAULT 'AGENDADA',
    observacao      TEXT,
    CONSTRAINT fk_consulta_medico   FOREIGN KEY (codigo_medico)   REFERENCES medico(codigo),
    CONSTRAINT fk_consulta_paciente FOREIGN KEY (codigo_paciente) REFERENCES paciente(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SELECT 'Banco de dados "clinica" criado com sucesso!' AS resultado;
