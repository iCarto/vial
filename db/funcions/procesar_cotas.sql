BEGIN;

-- definition
DROP TABLE IF EXISTS inventario.cotas;
CREATE TABLE inventario.cotas (
       gid serial,
       codigo_carretera varchar(4),
       codigo_municipio varchar(5),
       tramo varchar(1),
       pk_inicial float,
       pk_final float,
       longitud integer,
       fecha_actualizacion date,
       valor double precision, -- cota max
       valor_max double precision, --cota max
       valor_min double precision, --cota min
       observaciones text,
       PRIMARY KEY(gid),
       FOREIGN KEY (codigo_carretera) REFERENCES inventario.carreteras (numero)
               ON DELETE CASCADE
               ON UPDATE CASCADE
);

-- populate it
INSERT INTO inventario.cotas(
       SELECT nextval('inventario.cotas_gid_seq') AS gid,
              "numero_inv" AS codigo_carretera,
              to_char("cod_mun_lu", 'FM99999') AS codigo_municipio,
              "tramo" AS tramo,
              "pk_ini" AS pk_inicial,
              "pk_fin" AS pk_final,
              "longitud_t" AS longitud,
              '2012-06-01' AS fecha_actualizacion,
              "cota_fin" AS valor,
              "cota_fin" AS valor_max,
              "cota_ini" AS valor_min,
              NULL AS observaciones
       FROM inventario.cotas_tmp
);

DROP TABLE IF EXISTS inventario.cotas_tmp;

-- linear referencing
SELECT AddGeometryColumn('inventario', 'cotas', 'the_geom', '25829', 'MULTILINESTRINGM', 3);
ALTER TABLE inventario.cotas DROP CONSTRAINT enforce_geotype_the_geom;
SELECT inventario.update_geom_line_all('inventario', 'cotas');

-- triggers
DROP TRIGGER IF EXISTS update_geom_cotas ON inventario.cotas;
CREATE TRIGGER update_geom_cotas
       BEFORE UPDATE OR INSERT
       ON inventario.cotas FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_geom_line_on_pk_change();

DROP TRIGGER IF EXISTS update_longitud ON inventario.cotas;
CREATE TRIGGER update_longitud
       BEFORE UPDATE OR INSERT
       ON inventario.cotas FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_longitud();

COMMIT;
