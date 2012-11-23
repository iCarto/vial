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

-- indexes
CREATE INDEX carretera_municipio_codigo_carretera
       ON inventario.carretera_municipio USING BTREE(codigo_carretera);
CREATE INDEX carretera_municipio_codigo_municipio
       ON inventario.carretera_municipio USING BTREE(codigo_municipio);
CREATE INDEX carretera_municipio_orden_tramo
       ON inventario.carretera_municipio USING BTREE(orden_tramo);
CREATE INDEX carretera_municipio_codigo_carretera_concello
       ON inventario.carretera_municipio USING BTREE(codigo_carretera, codigo_municipio);
VACUUM ANALYZE inventario.carretera_municipio;
