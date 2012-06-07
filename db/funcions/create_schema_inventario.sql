--DROP DATABASE IF EXISTS vias_obras;
--CREATE DATABASE vias_obras OWNER postgres TEMPLATE template_postgis;

DROP SCHEMA IF EXISTS inventario CASCADE;
CREATE SCHEMA inventario;

DROP TABLE IF EXISTS inventario.carreteras_concellos;
CREATE TABLE inventario.carreteras_concellos(
       gid SERIAL,
       codigo_carretera varchar(4),
       codigo_concello varchar(5),
       CONSTRAINT pk_carreteras_concellos PRIMARY KEY (codigo_concello, codigo_carretera)
);

DROP TABLE IF EXISTS inventario.municipio_codigo;
CREATE TABLE inventario.municipio_codigo(
       codigo varchar(5),
       nombre varchar (21),
       CONSTRAINT pk_municipio_codigo PRIMARY KEY (codigo)
);
