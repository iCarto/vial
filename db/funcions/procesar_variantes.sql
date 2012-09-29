BEGIN;

-- definition
DROP TABLE IF EXISTS inventario.variantes;
CREATE TABLE inventario.variantes(
       gid serial,
       codigo_carretera varchar(4),
       codigo_municipio varchar(5),
       tramo varchar(1),
       orden_variante integer,
       orden_variante_tramo integer,
       pk_inicial float,
       pk_final float,
       origen_via_pk float,
       final_via_pk float,
       longitud integer,
       estado varchar(10),
       ancho_pavimento double precision,
       tipo_pavimento varchar(6),
       observaciones text,
       PRIMARY KEY(gid),
       FOREIGN KEY(codigo_carretera) REFERENCES inventario.carreteras(numero) ON DELETE CASCADE
);
SELECT AddGeometryColumn('inventario', 'variantes', 'the_geom', '25829', 'MULTILINESTRING', 2);

INSERT INTO inventario.variantes(
       SELECT nextval('inventario.variantes_gid_seq') AS gid,
              "numero_inv" AS codigo_carretera,
              to_char("cod_mun_lu", 'FM99999') AS codigo_municipio,
              "tramo" AS tramo,
              "cod_rv_o1" AS orden_variante,
              "cod_rv_o2" AS orden_variante_tramo,
              "pk_ini" AS pk_inicial,
              "pk_fin" AS pk_final,
              "pk_l_ini" AS origen_via_pk,
              "pk_l_fin" AS final_via_pk,
              "longitud__" AS longitud,
              "estado" AS estado,
              "ancho_medi" AS ancho_pavimento,
              "tipo_de_pa" AS tipo_pavimento,
              "observacio" AS observaciones,
              "the_geom" AS the_geom
        FROM inventario.variantes_rampas_tmp
        WHERE tipo=2 --1=rampas, 2=variantes
);

UPDATE inventario.variantes
       SET estado = 'USO'
       WHERE (estado IS NULL) OR (estado <> 'DESUSO' AND estado <> 'ABANDONO');
UPDATE inventario.variantes
       SET estado = 'DESUSO'
       WHERE estado = 'ABANDONO';

-- triggers

DROP TRIGGER IF EXISTS update_longitud ON inventario.variantes;
CREATE TRIGGER update_longitud
       BEFORE UPDATE OR INSERT
       ON inventario.variantes FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_longitud();

COMMIT;
