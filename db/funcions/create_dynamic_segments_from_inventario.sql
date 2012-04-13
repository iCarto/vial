
SELECT AddGeometryColumn('inventario', 'ancho_plataforma', 'the_geom', '23029', 'MULTILINESTRINGM', 3);

UPDATE inventario.ancho_plataforma AS a2 SET the_geom = (
       SELECT ST_CollectionExtract(ST_Locate_Between_Measures(c.the_geom,
                                a.origentram,
                                a.finaltramo), 2)
       FROM inventario.carreteras AS c, inventario.ancho_plataforma AS a
       WHERE c.codigo = a.numeroinve AND a.gid = a2.gid)
;
