-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.update_geom_point_all(
       the_schema_name TEXT,
       the_table_name TEXT) RETURNS void AS $BODY$
BEGIN

EXECUTE  'UPDATE '||the_schema_name||'.'||the_table_name||' AS a2 SET the_geom = (
       SELECT (ST_Dump(the_geom)).geom
       FROM (SELECT ST_Locate_Along_Measure(c.the_geom, a.pk) AS the_geom
                FROM inventario.carreteras AS c, '||the_schema_name||'.'||the_table_name||' AS a
                WHERE c.numero = a.codigo_carretera
                      AND a.gid = a2.gid) AS point );';

END;
$BODY$ LANGUAGE plpgsql;

