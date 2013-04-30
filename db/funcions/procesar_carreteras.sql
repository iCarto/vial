BEGIN;

-- definition
DROP TABLE IF EXISTS inventario.carreteras;
CREATE TABLE inventario.carreteras (
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
SELECT AddGeometryColumn('inventario', 'carreteras', 'the_geom', '25829', 'MULTILINESTRINGM', '3');
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
              "pk_origen_" AS origen_via_pk,
              "final_via" AS final_via,
              "pk_final_v" AS final_via_pk,
              NULL AS categoria,
              "topografia" AS topografia,
              "tipo_suelo" AS tipo_suelo,
              "longitud" AS longitud,
              'LONG ANTIGUA ('||"long_ant"||')' AS observaciones,
              "the_geom" AS the_geom
       FROM inventario.carreteras_tmp
);

SELECT DropGeometryTable('inventario','carreteras_tmp');

-- calibrate
SELECT inventario.calibrate_carretera_all();

-- constraints
ALTER TABLE inventario.carreteras ADD FOREIGN KEY(numero)
      REFERENCES inventario.carreteras_lugo(numero)
      ON DELETE CASCADE;

-- indexes
CREATE INDEX carreteras_the_geom
       ON inventario.carreteras USING GIST(the_geom);
CREATE INDEX carreteras_codigo
       ON inventario.carreteras USING BTREE(codigo);

COMMIT;
