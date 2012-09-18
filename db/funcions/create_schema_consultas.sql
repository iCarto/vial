DROP SCHEMA IF EXISTS consultas CASCADE;
CREATE SCHEMA consultas;

DROP TABLE IF EXISTS consultas.consultas;
CREATE TABLE consultas.consultas(
       codigo character varying(5) NOT NULL,
       descripcion character varying(250),
       consulta character varying(1500),
       haswhere character varying(2),
       titulo character varying(250),
       subtitulo character varying(250),
       CONSTRAINT pk_consultas PRIMARY KEY (codigo)
);
