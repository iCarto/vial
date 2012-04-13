
SELECT AddGeometryColumn('inventario', 'inventario', 'the_geom', '23029', 'LINESTRING', 2);
ALTER TABLE inventario.inventario DROP CONSTRAINT enforce_geotype_the_geom;
ALTER TABLE inventario.inventario DROP CONSTRAINT enforce_dims_the_geom;

UPDATE inventario.inventario AS i2 SET the_geom = (
       SELECT (ST_Dump(ST_Locate_Between_Measures(c.the_geom,
                                i.origentram,
                                i.finaltramo))).geom
       FROM inventario.carreteras AS c, inventario.inventario AS i
       WHERE c.codigo = i.numeroinve AND i.gid = i2.gid)
;
