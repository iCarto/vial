
SELECT AddGeometryColumn('inventario', 'aforos', 'the_geom', 23029, 'POINT', 2);
ALTER TABLE inventario.aforos DROP CONSTRAINT enforce_geotype_the_geom;
ALTER TABLE inventario.aforos DROP CONSTRAINT enforce_dims_the_geom;

UPDATE inventario.aforos AS a2 SET the_geom = (
       SELECT (ST_Dump(the_geom)).geom
       FROM (SELECT ST_Locate_Along_Measure(c.the_geom, a.pk) AS the_geom
                FROM inventario.carreteras AS c, inventario.aforos AS a
                WHERE c.codigo = a.numeroinve
                      AND a.gid = a2.gid) AS point )
;
