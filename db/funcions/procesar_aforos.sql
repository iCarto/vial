BEGIN;

--definition
DROP TABLE IF EXISTS inventario.aforos;
CREATE TABLE inventario.aforos (
       gid SERIAL,
       codigo_carretera varchar(4),
       codigo_municipio varchar(5),
       tramo varchar(1),
       pk float,
       fecha date,
       valor float,
       PRIMARY KEY(gid)
);

-- populate it
INSERT INTO inventario.aforos (
       SELECT nextval('inventario.aforos_gid_seq') AS gid,
              "numero_inv" AS codigo_carretera,
              to_char("codigo_mun", 'FM99999') AS codigo_municipio,
              "tramo" AS tramo,
              "pk" AS pk,
              to_date(to_char("a_o", '9999'), 'yyyy') AS fecha,
              "imd" AS valor
       FROM inventario.aforos_tmp
);

-- linear referencing
SELECT AddGeometryColumn('inventario', 'aforos', 'the_geom', 23029, 'POINTM', 3);
ALTER TABLE inventario.aforos DROP CONSTRAINT enforce_geotype_the_geom;
SELECT inventario.update_geom_point_all('inventario', 'aforos');

-- triggers
DROP TRIGGER IF EXISTS update_geom_aforos ON inventario.aforos;
CREATE TRIGGER update_geom_aforos
       BEFORE UPDATE OR INSERT
       ON inventario.aforos FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_geom_point_on_pk_change();

DROP TABLE IF EXISTS inventario.aforos_tmp;

COMMIT;

