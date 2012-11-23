BEGIN;

-- definition
DROP TABLE IF EXISTS inventario.carreteras_lugo;
CREATE TABLE inventario.carreteras_lugo (
       gid serial,
       codigo text,
       numero text UNIQUE,
       intermunicipal boolean,
       denominacion text,
       pk_inicial float,
       pk_final float,
       origen_via text,
       origen_via_pk float,
       final_via text,
       final_via_pk float,
       categoria text,
       topografia text,
       tipo_suelo text,
       longitud integer,
       observaciones text,
       PRIMARY KEY(gid)
);
SELECT AddGeometryColumn('inventario', 'carreteras_lugo', 'the_geom', '25829', 'MULTILINESTRING', '2');

-- populate it
INSERT INTO inventario.carreteras_lugo(
       SELECT nextval('inventario.carreteras_lugo_gid_seq') AS gid,
              "codigo" AS codigo,
              "numero" AS numero,
              CAST(to_char("intermunic", '9') AS boolean) AS intermunicipal,
              "denominaci" AS denominacion,
              "pk_inicial" AS pk_inicial,
              "pk_final" AS pk_final,
              "origen_via" AS origen_via,
              "pk_origen_" AS origen_via_pk,
              "final_via" AS final_via,
              "pk_final_v" AS final_via_pk,
              NULL AS categoria,
              "topografia" AS topografia,
              "tipo_suelo" AS tipo_suelo,
              "longitud" AS longitud,
              NULL AS observaciones,
              "the_geom" AS the_geom
       FROM inventario.carreteras_tmp
);

-- indexes
CREATE INDEX carreteras_lugo_the_geom
       ON inventario.carreteras_lugo USING GIST(the_geom);
CREATE INDEX carreteras_lugo_codigo
       ON inventario.carreteras_lugo USING BTREE(codigo);
VACUUM ANALYZE inventario.carreteras_lugo;

-- triggers
DROP TRIGGER IF EXISTS update_longitud ON inventario.carreteras_lugo;
CREATE TRIGGER update_longitud
       BEFORE UPDATE OR INSERT
       ON inventario.carreteras_lugo FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_longitud();

DROP TRIGGER IF EXISTS update_codigo_carretera ON inventario.carreteras_lugo;
CREATE TRIGGER update_codigo_carretera
       BEFORE UPDATE OR INSERT
       ON inventario.carreteras_lugo FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_codigo_carretera();

DROP TRIGGER IF EXISTS mirror_carreteras_lugo ON inventario.carreteras_lugo;
CREATE TRIGGER mirror_carreteras_lugo
       AFTER UPDATE OR INSERT
       ON inventario.carreteras_lugo FOR EACH ROW
       EXECUTE PROCEDURE inventario.mirror_carreteras_lugo();

COMMIT;
