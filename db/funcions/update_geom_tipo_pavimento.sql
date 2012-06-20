-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.update_geom_tipo_pavimento() RETURNS void AS $BODY$
BEGIN

        UPDATE inventario.tipo_pavimento AS p2 SET the_geom = (
               SELECT ST_CollectionExtract(ST_Locate_Between_Measures(c.the_geom,
                                p.origenpavi,
                                p.finalpavim), 2)
               FROM inventario.rede_carreteras AS c, inventario.tipo_pavimento AS p
               WHERE c.codigo = p.carretera AND p.gid = p2.gid);

END;
$BODY$ LANGUAGE plpgsql;
