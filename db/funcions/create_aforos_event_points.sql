
SELECT AddGeometryColumn('inventario', 'aforos', 'the_geom', 23029, 'POINTM', 3);

UPDATE inventario.aforos AS a2 SET the_geom = (
       SELECT (ST_Dump(the_geom)).geom
       FROM (SELECT ST_Locate_Along_Measure(c.the_geom, a.pk) AS the_geom
                FROM inventario.red_carreteras AS c, inventario.aforos AS a
                WHERE c.numero = a.numeroinve
                      AND a.gid = a2.gid) AS point )
;
