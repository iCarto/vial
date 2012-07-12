BEGIN;

DROP TABLE IF EXISTS inventario.rede_carreteras;
CREATE TABLE inventario.rede_carreteras (
       gid serial,
       codigo_provincial varchar(9),
       codigo varchar(4),
       denominacion varchar(256),
       pk_inicial float,
       pk_final float,
       origen_via varchar(256),
       origen_via_pk varchar(24),
       final_via varchar(256),
       final_via_pk varchar(24),
       categoria varchar(18),
       observaciones text,
       PRIMARY KEY(gid)
);
SELECT AddGeometryColumn('inventario', 'rede_carreteras', 'the_geom', '23029', 'MULTILINESTRINGM', '3');

INSERT INTO inventario.rede_carreteras(
       SELECT nextval('inventario.rede_carreteras_gid_seq') AS gid,
              "código_pr" AS codigo_provincial,
              "codigo" AS codigo,
              "denominaci" AS denominacion,
              "pk_origen" AS pk_inicial,
              "pk_final" AS pk_final,
              "origenvia" AS origen_via,
              "origenpk" AS origen_via_pk,
              "finalvia" AS final_via,
              "finalpk" AS final_via_pk,
              NULL AS categoria,
              "observacio" AS observaciones,
              "the_geom" AS the_geom
       FROM inventario.rede_carreteras_tmp
);

SELECT DropGeometryTable('inventario','rede_carreteras_tmp');

COMMIT;
