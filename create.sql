CREATE TABLE  IF NOT EXISTS paciente (
  id int AUTO_INCREMENT PRIMARY KEY,
  nome varchar(50),
  sobrenome varchar(50),
  rg varchar(30),
  data_alta date,
  endereco varchar(100)
);

CREATE TABLE IF NOT EXISTS dentista (
id int auto_increment primary key,
nome varchar(32),
sobrenome varchar(32),
matricula varchar(32));



CREATE TABLE IF NOT EXISTS consulta(
 id int AUTO_INCREMENT PRIMARY KEY,
 dentista_id int,
 paciente_id int,
 data_consulta Date,
 FOREIGN KEY (dentista_id) REFERENCES dentista(id),
 FOREIGN KEY (paciente_id) REFERENCES paciente(id)
);


CREATE TABLE IF NOT EXISTS usuario(
  id int AUTO_INCREMENT PRIMARY KEY,
  usuario varchar(50),
  senha varchar(50),
  funcao varchar(50)
);

