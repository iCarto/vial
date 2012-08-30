BEGIN;

-- definition
DROP TABLE IF EXISTS inventario.actuaciones;
CREATE TABLE inventario.actuaciones(
       gid SERIAL,
       codigo_actuacion varchar(4) UNIQUE,
       codigo_carretera varchar(4),
       pk_inicial float,
       pk_final float,
       tipo varchar(24),
       descripcion varchar(64),
       titulo_proyecto varchar(64),
       importe float,
       contratista varchar(24),
       fecha date,
       observaciones text,
       CONSTRAINT pk_actuaciones PRIMARY KEY(gid)
);

-- populate it: This is fake data!
INSERT INTO inventario.actuaciones (codigo_actuacion, codigo_carretera, pk_inicial, pk_final)
       VALUES ('0000', '0101', 0, 2);
INSERT INTO inventario.actuaciones (codigo_actuacion, codigo_carretera, pk_inicial, pk_final)
       VALUES ('0001', '0102', 0, 2);
INSERT INTO inventario.actuaciones (codigo_actuacion, codigo_carretera, pk_inicial, pk_final)
       VALUES ('0002', '0103', 0, 2);

-- linear referencing
SELECT AddGeometryColumn('inventario', 'actuaciones', 'the_geom', '23029', 'MULTILINESTRINGM', 3);
ALTER TABLE inventario.actuaciones DROP CONSTRAINT enforce_geotype_the_geom;
SELECT inventario.update_geom_all('inventario', 'actuaciones');

-- triggers
DROP TRIGGER IF EXISTS update_geom_actuaciones ON inventario.actuaciones;
CREATE TRIGGER update_geom_actuaciones
       BEFORE UPDATE OR INSERT
       ON inventario.actuaciones FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_geom_on_pk_change();

COMMIT;
