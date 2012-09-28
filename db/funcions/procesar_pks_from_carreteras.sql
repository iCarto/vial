DROP TABLE IF EXISTS public.pks;
DROP SEQUENCE IF EXISTS pks_pk_seq;

CREATE SEQUENCE pks_pk_seq INCREMENT BY 1 START WITH 1;
CREATE TABLE public.pks(
        gid serial,
        codigo_carretera varchar(4),
        codigo_municipio varchar(5),
        pk int DEFAULT nextval('pks_pk_seq') NOT NULL,
        PRIMARY KEY(gid),
        FOREIGN KEY(codigo_carretera) REFERENCES inventario.carreteras(numero) ON DELETE CASCADE
);

SELECT inventario.create_pks();
SELECT AddGeometryColumn('public', 'pks', 'the_geom', '25829', 'POINTM', 3);
SELECT inventario.update_geom_point_all('public', 'pks');
