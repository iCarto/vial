DROP TABLE IF EXISTS inventario.carretera_municipio;
CREATE TABLE inventario.carretera_municipio(
       id SERIAL,
       codigo_carretera text,
       codigo_municipio text,
       orden_tramo text,
       pk_inicial_tramo float,
       pk_final_tramo float,
       longitud_tramo integer,
       observaciones_tramo text,
       CONSTRAINT pk_carretera_municipio PRIMARY KEY (codigo_municipio, codigo_carretera, orden_tramo),
       FOREIGN KEY (codigo_carretera) REFERENCES inventario.carreteras (numero)
               ON DELETE CASCADE
               ON UPDATE CASCADE
);
