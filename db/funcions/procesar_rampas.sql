BEGIN;

-- definition
DROP TABLE IF EXISTS inventario.rampas;
CREATE TABLE inventario.rampas(
       gid serial,
       codigo_carretera text,
       codigo_municipio text,
       tramo text,
       orden_rampa integer,
       orden_rampa_tramo integer,
       pk_inicial float,
       pk_final float,
       origen_via_pk float,
       longitud integer,
       estado text,
       ancho_plataforma double precision,
       tipo_pavimento text,
       observaciones text,
       PRIMARY KEY(gid),
       FOREIGN KEY(codigo_carretera) REFERENCES inventario.carreteras(numero)
               ON DELETE CASCADE
               ON UPDATE CASCADE
);
SELECT AddGeometryColumn('inventario', 'rampas', 'the_geom', '25829', 'MULTILINESTRING', 2);

INSERT INTO inventario.rampas(
       SELECT nextval('inventario.rampas_gid_seq') AS gid,
              "numero_inv" AS codigo_carretera,
              to_char("cod_mun_lu", 'FM99999') AS codigo_municipio,
              "tramo" AS tramo,
              "cod_rv_o1" AS orden_rampa,
              "cod_rv_o2" AS orden_rampa_tramo,
              "pk_ini" AS pk_inicial,
              "pk_fin" AS pk_final,
              "pk_l_ini" AS origen_via_pk,
              "longitud__" AS longitud,
              "estado" AS estado,
              "ancho_medi" AS ancho_plataforma,
              "tipo_de_pa" AS tipo_pavimento,
              "observacio" AS observaciones,
              "the_geom" AS the_geom
        FROM inventario.variantes_rampas_tmp
        WHERE tipo=1 -- 1=rampas, 2=variantes
);

UPDATE inventario.rampas
       SET estado = 'USO'
       WHERE (estado IS NULL) OR (estado <> 'DESUSO' AND estado <> 'ABANDONO');
UPDATE inventario.rampas
       SET estado = 'DESUSO'
       WHERE estado = 'ABANDONO';

-- indexes
CREATE INDEX rampas_the_geom
       ON inventario.rampas USING GIST(the_geom);
CREATE INDEX rampas_codigo_carretera
       ON inventario.rampas USING BTREE(codigo_carretera);
CREATE INDEX rampas_codigo_municipio
       ON inventario.rampas USING BTREE(codigo_municipio);
CREATE INDEX rampas_codigo_carretera_concello
       ON inventario.rampas USING BTREE(codigo_carretera, codigo_municipio);

-- triggers
DROP TRIGGER IF EXISTS update_longitud ON inventario.rampas;
CREATE TRIGGER update_longitud
       BEFORE UPDATE OR INSERT
       ON inventario.rampas FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_longitud();

COMMIT;
