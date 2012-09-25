-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.calibrate_carretera_all() RETURNS void AS $BODY$
BEGIN

         UPDATE inventario.carreteras AS c2 SET the_geom = (
                 SELECT ST_AddMeasure(the_geom, pk_inicial, pk_final)
                 FROM inventario.carreteras AS c
                 WHERE c.gid = c2.gid);

END;
$BODY$ LANGUAGE plpgsql;
