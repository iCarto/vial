
DROP TABLE IF EXISTS inventario.municipio_codigo;
CREATE TABLE inventario.municipio_codigo(
       codigo text,
       nombre text,
       CONSTRAINT pk_municipio_codigo PRIMARY KEY (codigo)
);
