
ALTER TABLE inventario.carreteras DROP CONSTRAINT enforce_geotype_the_geom;
ALTER TABLE inventario.carreteras DROP CONSTRAINT enforce_dims_the_geom;
UPDATE inventario.carreteras AS c2 SET the_geom = (
       SELECT ST_AddMeasure(the_geom, pk_origen, pk_final)
       FROM inventario.carreteras AS c
       WHERE c.gid = c2.gid)
;
