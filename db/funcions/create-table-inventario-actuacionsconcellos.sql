
DROP TABLE IF EXISTS inventario.actuacions_concellos;
CREATE TABLE inventario.actuacions_concellos(
       id SERIAL,
       codigo_actuacion varchar(4),
       codigo_concello varchar(5),
       CONSTRAINT pk_actuacions_concellos PRIMARY KEY (codigo_concello, codigo_actuacion)
);
