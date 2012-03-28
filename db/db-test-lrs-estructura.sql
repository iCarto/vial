-- createdb -h localhost -p 5432 -U postgres -w -O postgres -T template_postgis test_lrs
--DROP DATABASE IF EXISTS test_lrs;
--CREATE DATABASE test_lrs
--       WITH OWNER = 'postgresql'
--       TEMPLATE = 'template_postgis';

DROP TABLE IF EXISTS public.routes;
CREATE TABLE public.routes (
       id serial NOT NULL,
       id_route CHARACTER(4),
       measure_start float,
       measure_end float,
       CONSTRAINT pk_routes PRIMARY KEY (id)
);

DROP TABLE IF EXISTS public.events_point;
CREATE TABLE public.events_point (
       id serial NOT NULL,
       id_route CHARACTER(4),
       measure FLOAT,
       CONSTRAINT pk_events_point PRIMARY KEY (id)
);

DROP TABLE IF EXISTS public.events_line;
CREATE TABLE public.events_line(
       id serial NOT NULL,
       id_route CHARACTER(4),
       measure_start FLOAT,
       measure_end FLOAT,
       CONSTRAINT pk_events_line PRIMARY KEY (id)
);

--SELECT AddGeometryColumn ('public','routes','the_geom',23029,'LINESTRING',2);
--ALTER TABLE public.routes
--      DROP CONSTRAINT enforce_geotype_the_geom;
--ALTER TABLE public.routes
--      DROP CONSTRAINT enforce_dims_the_geom;
-- do it manually to avoid the constraints creation, as they prevent us to use routes (LINESTRINGM)
ALTER TABLE public.routes ADD the_geom geometry;
DELETE FROM public.geometry_columns WHERE f_table_schema='public' AND f_table_name='routes';
INSERT INTO geometry_columns(
       f_table_catalog, f_table_schema, f_table_name, f_geometry_column, coord_dimension, srid, "type")
       VALUES ('', 'public', 'routes', 'the_geom', 2, 23029, 'LINESTRING');

-- SELECT AddGeometryColumn ('public','events_point','the_geom',23029,'POINT',2);
-- ALTER TABLE public.events_point
--       DROP CONSTRAINT enforce_geotype_the_geom;
-- ALTER TABLE public.events_point
--       DROP CONSTRAINT enforce_dims_the_geom;
-- do it manually to avoid the constraints creation, as they prevent us to use routes (LINESTRINGM)
ALTER TABLE public.events_point ADD the_geom geometry;
DELETE FROM geometry_columns WHERE f_table_schema='public' AND f_table_name='events_point';
INSERT INTO geometry_columns(
       f_table_catalog, f_table_schema, f_table_name, f_geometry_column, coord_dimension, srid, "type")
       VALUES ('', 'public', 'events_point', 'the_geom', 2, 23029, 'POINT');

-- SELECT AddGeometryColumn('public','events_line','the_geom',23029,'LINESTRING',2);
-- ALTER TABLE public.events_line
--       DROP CONSTRAINT enforce_geotype_the_geom;
-- ALTER TABLE public.events_line
--       DROP CONSTRAINT enforce_dims_the_geom;
-- do it manually to avoid the constraints creation, as they prevent us to use routes (LINESTRINGM)
ALTER TABLE public.events_line ADD the_geom geometry;
DELETE FROM geometry_columns WHERE f_table_schema='public' AND f_table_name='events_line';
INSERT INTO geometry_columns(
       f_table_catalog, f_table_schema, f_table_name, f_geometry_column, coord_dimension, srid, "type")
       VALUES ('', 'public', 'events_line', 'the_geom', 2, 23029, 'LINESTRING');

-- insert data

INSERT INTO public.routes (id_route, measure_start, measure_end, the_geom)
       VALUES ('GZ01', 8, 32,
              ST_GeomFromEWKT('SRID=23029;LINESTRING(1 1, 2 2, 4 4)'));

INSERT INTO public.events_point (id_route, measure) VALUES ('GZ01', 16);
INSERT INTO public.events_point (id_route, measure) VALUES ('GZ02', 24);

INSERT INTO public.events_line (id_route, measure_start, measure_end)
       VALUES ('GZ01', 10, 18);

-- create route: calibrate line by setting the ends

UPDATE public.routes SET the_geom = (
       SELECT ST_AddMeasure(the_geom, measure_start, measure_end)
       FROM public.routes);

-- assign computed interpolated point from route
UPDATE events_point SET the_geom = (
       SELECT ST_Line_Interpolate_Point(r.the_geom,
              ((measure - r.measure_start)/(r.measure_end - r.measure_start)))
       FROM public.routes AS r);

-- assign computed interpolated line from route
UPDATE events_line SET the_geom = (
       SELECT ST_Line_Substring(r.the_geom,
              ((e.measure_start - r.measure_start) / (r.measure_end - r.measure_start)),
              ((e.measure_end - r.measure_start) / (r.measure_end - r.measure_start)))
       FROM public.routes AS r, public.events_line AS e);
