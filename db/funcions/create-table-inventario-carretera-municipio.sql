DROP TABLE IF EXISTS inventario.carretera_municipio;
CREATE TABLE inventario.carretera_municipio(
       id SERIAL,
       codigo_carretera varchar(4),
       codigo_municipio varchar(5),
       orden_tramo varchar(1),
       pk_inicial_tramo float,
       pk_final_tramo float,
       longitud_tramo float,
       observaciones_tramo text,
       CONSTRAINT pk_carretera_municipio PRIMARY KEY (codigo_municipio, codigo_carretera, orden_tramo),
       FOREIGN KEY (codigo_carretera) REFERENCES inventario.carreteras (numero) ON DELETE CASCADE
);

DROP TRIGGER IF EXISTS update_longitud_carretera_municipio ON inventario.carretera_municipio;
CREATE TRIGGER update_longitud_carretera_municipio
       BEFORE UPDATE OR INSERT
       ON inventario.carretera_municipio FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_longitud_carretera_municipio();
