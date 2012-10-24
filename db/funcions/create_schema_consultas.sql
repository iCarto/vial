DROP SCHEMA IF EXISTS consultas CASCADE;
CREATE SCHEMA consultas;

DROP TABLE IF EXISTS consultas.consultas;
CREATE TABLE consultas.consultas(
       codigo varchar NOT NULL,
       descripcion varchar,
       consulta varchar,
       haswhere varchar,
       titulo varchar,
       subtitulo varchar,
       CONSTRAINT pk_consultas PRIMARY KEY (codigo)
);

DROP TABLE IF EXISTS consultas.aforos;
CREATE TABLE consultas.aforos(
       valor_minimo INTEGER,
       valor_maximo INTEGER
);
