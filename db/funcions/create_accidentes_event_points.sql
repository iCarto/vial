
SELECT AddGeometryColumn('inventario', 'accidentes', 'the_geom', 23029, 'POINTM', 3);

UPDATE inventario.accidentes AS a2 SET the_geom = (
       SELECT (ST_Dump(the_geom)).geom
       FROM (SELECT ST_Locate_Along_Measure(c.the_geom, a.pk) AS the_geom
                FROM inventario.rede_carreteras AS c, inventario.accidentes AS a
                WHERE c.codigo = a.codigo
                      AND a.id_acciden = a2.id_acciden) AS point )
;
