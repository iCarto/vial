DROP TABLE IF EXISTS inventario.pks_1000;
CREATE TABLE inventario.pks_1000(
        gid serial,
        codigo_carretera text,
        pk int NOT NULL,
        PRIMARY KEY(gid),
        FOREIGN KEY(codigo_carretera) REFERENCES inventario.carreteras(numero) ON DELETE CASCADE
);

SELECT inventario.create_pks_1000();
SELECT AddGeometryColumn('inventario', 'pks_1000', 'the_geom', '25829', 'POINT', 2);
ALTER TABLE inventario.pks_1000 DROP CONSTRAINT enforce_geotype_the_geom;
ALTER TABLE inventario.pks_1000 DROP CONSTRAINT enforce_dims_the_geom;
SELECT inventario.update_geom_point_all('inventario', 'pks_1000');

-- indexes
CREATE INDEX pks_1000_the_geom
       ON inventario.pks_1000 USING GIST(the_geom);
CREATE INDEX pks_1000_codigo_carretera
       ON inventario.pks_1000 USING BTREE(codigo_carretera);

-- triggers
DROP TRIGGER IF EXISTS update_geom_pks_1000 ON inventario.pks_1000;
CREATE TRIGGER update_geom_pks_1000
       BEFORE UPDATE OR INSERT
       ON inventario.pks_1000 FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_geom_point_on_pk_change();
