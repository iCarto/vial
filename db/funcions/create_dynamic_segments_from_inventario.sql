
SELECT AddGeometryColumn('inventario', 'ancho_plataforma', 'the_geom', '23029', 'LINESTRING', 2);
ALTER TABLE inventario.ancho_plataforma DROP CONSTRAINT enforce_geotype_the_geom;
ALTER TABLE inventario.ancho_plataforma DROP CONSTRAINT enforce_dims_the_geom;

UPDATE inventario.ancho_plataforma AS a2 SET the_geom = (
       SELECT (ST_Dump(ST_Locate_Between_Measures(c.the_geom,
                                a.origentram,
                                a.finaltramo))).geom
       FROM inventario.carreteras AS c, inventario.ancho_plataforma AS a
       WHERE c.codigo = a.numeroinve AND a.gid = a2.gid)
;
