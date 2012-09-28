DROP TABLE IF EXISTS inventario.pks_1000;
DROP SEQUENCE IF EXISTS pks_1000_pk_seq;

CREATE SEQUENCE pks_1000_pk_seq INCREMENT BY 1 START WITH 1;
CREATE TABLE inventario.pks_1000(
        gid serial,
        codigo_carretera varchar(4),
        codigo_municipio varchar(5),
        pk int DEFAULT nextval('pks_1000_pk_seq') NOT NULL,
        PRIMARY KEY(gid),
        FOREIGN KEY(codigo_carretera) REFERENCES inventario.carreteras(numero) ON DELETE CASCADE
);

SELECT inventario.create_pks_1000();
SELECT AddGeometryColumn('inventario', 'pks_1000', 'the_geom', '25829', 'POINT', 2);
ALTER TABLE inventario.pks_1000 DROP CONSTRAINT enforce_geotype_the_geom;
ALTER TABLE inventario.pks_1000 DROP CONSTRAINT enforce_dims_the_geom;
SELECT inventario.update_geom_point_all('inventario', 'pks_1000');
