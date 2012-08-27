DROP TABLE IF EXISTS inventario.carretera_municipio;
CREATE TABLE inventario.carretera_municipio(
       id SERIAL,
       codigo_carretera varchar(4),
       codigo_municipio varchar(5),
       pk_inicial float8,
       pk_final float8,
       observaciones text,
       CONSTRAINT pk_carretera_municipio PRIMARY KEY (codigo_municipio, codigo_carretera)
);
