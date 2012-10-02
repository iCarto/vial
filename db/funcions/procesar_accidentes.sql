BEGIN;

--definition
DROP TABLE IF EXISTS inventario.accidentes;
CREATE TABLE inventario.accidentes (
       gid SERIAL,
       codigo_carretera text,
       codigo_municipio text,
       tramo text,
       pk float,
       fecha date,
       valor text,
       PRIMARY KEY(gid),
       FOREIGN KEY (codigo_carretera) REFERENCES inventario.carreteras (numero)
               ON DELETE CASCADE
               ON UPDATE CASCADE
);

-- populate it
INSERT INTO inventario.accidentes (
       SELECT nextval('inventario.accidentes_gid_seq') AS gid,
              "numero" AS codigo_carretera,
              to_char("cod_mun_lu", 'FM99999') AS codigo_municipio,
              "tramo" AS tramo,
              "pk" AS pk,
              "fecha" AS fecha,
              "id_acciden" AS valor
       FROM inventario.accidentes_tmp
);

-- linear referencing
SELECT AddGeometryColumn('inventario', 'accidentes', 'the_geom', 25829, 'POINT', 2);
ALTER TABLE inventario.accidentes DROP CONSTRAINT enforce_geotype_the_geom;
ALTER TABLE inventario.accidentes DROP CONSTRAINT enforce_dims_the_geom;
SELECT inventario.update_geom_point_all('inventario', 'accidentes');

-- triggers
DROP TRIGGER IF EXISTS update_geom_accidentes ON inventario.accidentes;
CREATE TRIGGER update_geom_accidentes
       BEFORE UPDATE OR INSERT
       ON inventario.accidentes FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_geom_point_on_pk_change();

DROP TABLE IF EXISTS inventario.accidentes_tmp;

COMMIT;
