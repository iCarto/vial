BEGIN;

-- definition
DROP TABLE IF EXISTS inventario.carreteras;
CREATE TABLE inventario.carreteras (
       gid serial,
       codigo varchar(9) UNIQUE,
       numero varchar(4) UNIQUE,
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
SELECT AddGeometryColumn('inventario', 'carreteras', 'the_geom', '23029', 'MULTILINESTRINGM', '3');
ALTER TABLE inventario.carreteras DROP CONSTRAINT enforce_geotype_the_geom;
ALTER TABLE inventario.carreteras DROP CONSTRAINT enforce_dims_the_geom;

-- populate it
INSERT INTO inventario.carreteras(
       SELECT nextval('inventario.carreteras_gid_seq') AS gid,
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
       FROM inventario.carreteras_tmp
);

SELECT DropGeometryTable('inventario','carreteras_tmp');

-- calibrate
SELECT inventario.calibrate_carretera_all();

-- triggers
DROP TRIGGER IF EXISTS calibrate_carretera_and_tramos ON inventario.carreteras;
CREATE TRIGGER calibrate_carretera_and_tramos
       AFTER UPDATE OR INSERT
       ON inventario.carreteras FOR EACH ROW
       EXECUTE PROCEDURE inventario.calibrate_carretera_and_tramos();

-- constraints
ALTER TABLE inventario.carreteras ADD FOREIGN KEY(numero)
      REFERENCES inventario.carreteras_lugo(numero)
      ON DELETE CASCADE;

COMMIT;
