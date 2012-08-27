BEGIN;

DROP TABLE IF EXISTS inventario.red_carreteras;
CREATE TABLE inventario.red_carreteras (
       gid serial,
       codigo varchar(9),
       numero varchar(4),
       intermunicipal boolean,
       denominacion varchar(256),
       pk_inicial float,
       pk_final float,
       origen_via varchar(256),
       origen_via_pk varchar(24),
       final_via varchar(256),
       final_via_pk varchar(24),
       categoria varchar(18),
       topografia varchar(5),
       tipo_suelo varchar(5),
       longitud float,
       observaciones text,
       PRIMARY KEY(gid)
);
SELECT AddGeometryColumn('inventario', 'red_carreteras', 'the_geom', '23029', 'MULTILINESTRING', '2');

INSERT INTO inventario.red_carreteras(
       SELECT nextval('inventario.red_carreteras_gid_seq') AS gid,
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
              NULL AS observaciones,
              "longitud" AS longitud,
              "the_geom" AS the_geom
       FROM inventario.red_carreteras_tmp
);

SELECT DropGeometryTable('inventario','red_carreteras_tmp');

COMMIT;
