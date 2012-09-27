BEGIN;

-- definition
DROP TABLE IF EXISTS inventario.carreteras_lugo;
CREATE TABLE inventario.carreteras_lugo (
       gid serial,
       codigo varchar(9) UNIQUE,
       numero varchar(4) UNIQUE,
       intermunicipal boolean,
       denominacion varchar(256),
       pk_inicial float,
       pk_final float,
       origen_via varchar(256),
       origen_via_pk float,
       final_via varchar(256),
       final_via_pk float,
       categoria varchar(18),
       topografia varchar(5),
       tipo_suelo varchar(5),
       longitud float,
       observaciones text,
       PRIMARY KEY(gid)
);
SELECT AddGeometryColumn('inventario', 'carreteras_lugo', 'the_geom', '23029', 'MULTILINESTRING', '2');

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
              "origen_v_1" AS origen_via_pk,
              "final_via" AS final_via,
              "final_via_" AS final_via_pk,
              NULL AS categoria,
              "topografia" AS topografia,
              "tipo_suelo" AS tipo_suelo,
              "longitud" AS longitud,
              NULL AS observaciones,
              "the_geom" AS the_geom
       FROM inventario.carreteras_tmp
);

-- triggers
DROP TRIGGER IF EXISTS update_longitud ON inventario.carreteras_lugo;
CREATE TRIGGER update_longitud
       BEFORE UPDATE OR INSERT
       ON inventario.carreteras_lugo FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_longitud();

DROP TRIGGER IF EXISTS mirror_carreteras_lugo ON inventario.carreteras_lugo;
CREATE TRIGGER mirror_carreteras_lugo
       AFTER UPDATE OR INSERT
       ON inventario.carreteras_lugo FOR EACH ROW
       EXECUTE PROCEDURE inventario.mirror_carreteras_lugo();

COMMIT;
