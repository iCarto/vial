ALTER TABLE inventario.red_carreteras DROP CONSTRAINT enforce_geotype_the_geom;
ALTER TABLE inventario.red_carreteras DROP CONSTRAINT enforce_dims_the_geom;
UPDATE inventario.red_carreteras AS c2 SET the_geom = (
       SELECT ST_AddMeasure(the_geom, pk_inicial, pk_final)
       FROM inventario.red_carreteras AS c
       WHERE c.gid = c2.gid)
;
