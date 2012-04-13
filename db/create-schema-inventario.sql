--DROP DATABASE IF EXISTS vias_obras;
--CREATE DATABASE vias_obras OWNER postgres TEMPLATE template_postgis;

DROP SCHEMA IF EXISTS inventario CASCADE;
CREATE SCHEMA inventario;

-- DROP TABLE IF EXISTS inventario.carreteras;
-- CREATE TABLE inventario.carreteras(
--        gid SERIAL,
--        carretera_id NUMERIC(4),
--        carretera_id_provincial VARCHAR(15),
--        denominacion VARCHAR(50),
--        CONSTRAINT pk_carreteras PRIMARY KEY (gid),
--        CONSTRAINT unique_carretera_id UNIQUE(carretera_id),
--        CONSTRAINT unique_carretera_id_provincial UNIQUE(carretera_id_provincial)
-- );
-- DELETE FROM public.geometry_columns WHERE f_table_schema='inventario' AND f_table_name='carreteras';
-- SELECT AddGeometryColumn ('inventario','carreteras','the_geom',23029,'LINESTRING',2);

-- DROP TABLE IF EXISTS inventario.ancho_calzada;
-- CREATE TABLE inventario.ancho_calzada(
--        id SERIAL,
--        carretera_id NUMERIC(4),
--        ancho NUMERIC(3,3),
--        pk_inicial DOUBLE PRECISION,
--        pk_final DOUBLE PRECISION,
--        CONSTRAINT pk_ancho_calzada PRIMARY KEY(id)--,
--        --CONSTRAINT fk_ancho_carretera_id FOREIGN KEY(carretera_id)
--        --           REFERENCES inventario.carreteras (carretera_id)
-- );
-- -- COPY inventario.ancho_calzada (carretera_id, pk_inicial, pk_final)
-- --      FROM datos/inventario.csv
-- --           WITH CSV HEADER;

-- DROP TABLE IF EXISTS inventario.tipo_pavimento;
-- CREATE TABLE inventario.tipo_pavimento(
--        id SERIAL,
--        carretera_id NUMERIC(4),
--        pk_inicial NUMERIC(3,2),
--        pk_final NUMERIC(3,2),
--        CONSTRAINT pk_tipo_pavimento PRIMARY KEY(id),
--        CONSTRAINT fk_pavimento_carretera_id FOREIGN KEY(carretera_id)
--                   REFERENCES inventario.carreteras (carretera_id)
-- );

-- DROP TABLE IF EXISTS inventario.imds;
-- CREATE TABLE inventario.imds(
--        id SERIAL,
--        carretera_id NUMERIC(4),
--        imd NUMERIC(5),
--        fecha NUMERIC(4),
--        pk_inicial NUMERIC(3,2),
--        pk_final NUMERIC(3,2),
--        CONSTRAINT pk_imds PRIMARY KEY(id),
--        CONSTRAINT fk_pavimento_carretera_id FOREIGN KEY(carretera_id)
--                   REFERENCES inventario.carreteras (carretera_id)
-- );

-- DROP TABLE IF EXISTS inventario.accidentes;
-- CREATE TABLE inventario.accidentes(
--        id SERIAL,
--        carretera_id NUMERIC(4),
--        accidente_id NUMERIC(13),
--        pk NUMERIC(3,2),
--        fecha DATE, -- formato dia/mes/ano hora:minuto
--        tipo TEXT,
--        CONSTRAINT pk_accidentes PRIMARY KEY(id),
--        CONSTRAINT fk_accidentes_carretera_id FOREIGN KEY(carretera_id)
--                   REFERENCES inventario.carreteras (carretera_id)
-- );

