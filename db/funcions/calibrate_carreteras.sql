ALTER TABLE inventario.rede_carreteras DROP CONSTRAINT enforce_geotype_the_geom;
ALTER TABLE inventario.rede_carreteras DROP CONSTRAINT enforce_dims_the_geom;
UPDATE inventario.rede_carreteras AS c2 SET the_geom = (
       SELECT ST_AddMeasure(the_geom, pk_origen, pk_final)
       FROM inventario.rede_carreteras AS c
       WHERE c.gid = c2.gid)
;
