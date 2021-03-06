-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.update_geom_line_all(
       the_schema_name TEXT,
       the_table_name TEXT) RETURNS void AS $BODY$
BEGIN

        EXECUTE 'UPDATE '||the_schema_name||'.'||the_table_name||' AS a2 SET the_geom = (
               SELECT ST_CollectionExtract(ST_Locate_Between_Measures(c.the_geom,
                                a.pk_inicial,
                                a.pk_final), 2)
               FROM inventario.carreteras AS c, '||the_schema_name||'.'||the_table_name||' AS a
               WHERE c.numero = a.codigo_carretera AND a.gid = a2.gid);';

END;
$BODY$ LANGUAGE plpgsql;
