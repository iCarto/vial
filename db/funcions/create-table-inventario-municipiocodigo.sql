
DROP TABLE IF EXISTS inventario.municipio_codigo;
CREATE TABLE inventario.municipio_codigo(
       codigo varchar(5),
       nombre varchar (21),
       CONSTRAINT pk_municipio_codigo PRIMARY KEY (codigo)
);
