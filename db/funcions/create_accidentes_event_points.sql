
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
