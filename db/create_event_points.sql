
SELECT AddGeometryColumn('inventario', 'aforos', 'the_geom', 23029, 'POINT', 2);
ALTER TABLE inventario.aforos DROP CONSTRAINT enforce_geotype_the_geom;
ALTER TABLE inventario.aforos DROP CONSTRAINT enforce_dims_the_geom;

UPDATE inventario.aforos AS a2 SET the_geom = (
       SELECT (ST_Dump(the_geom)).geom
       FROM (SELECT ST_Locate_Along_Measure(c.the_geom, a.pk) AS the_geom
                FROM inventario.carreteras AS c, inventario.aforos AS a
                WHERE c.codigo = a.numeroinve
                      AND a.numeroinve = a2.numeroinve
                      AND a.tramo = a2.tramo) AS point )
;

SELECT AddGeometryColumn('inventario', 'accidentes', 'the_geom', 23029, 'POINT', 2);
ALTER TABLE inventario.accidentes DROP CONSTRAINT enforce_geotype_the_geom;
ALTER TABLE inventario.accidentes DROP CONSTRAINT enforce_dims_the_geom;

UPDATE inventario.accidentes AS a2 SET the_geom = (
       SELECT (ST_Dump(the_geom)).geom
       FROM (SELECT ST_Locate_Along_Measure(c.the_geom, a.pk) AS the_geom
                FROM inventario.carreteras AS c, inventario.accidentes AS a
                WHERE c.codigo = a.codigo
                      AND a.id_acciden = a2.id_acciden) AS point )
;
