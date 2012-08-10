DROP TABLE IF EXISTS inventario.actuacions;
CREATE TABLE inventario.actuacions(
       id SERIAL,
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
       CONSTRAINT pk_actuaciones PRIMARY KEY(id)
);
SELECT AddGeometryColumn('inventario', 'actuacions', 'the_geom', 23029, 'MULTILINESTRINGM', 3);
