-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.update_geom_ancho_plataforma_all() RETURNS void AS $BODY$
BEGIN

        UPDATE inventario.ancho_plataforma AS a2 SET the_geom = (
               SELECT ST_CollectionExtract(ST_Locate_Between_Measures(c.the_geom,
                                a.pk_inicial,
                                a.pk_final), 2)
               FROM inventario.rede_carreteras AS c, inventario.ancho_plataforma AS a
               WHERE c.codigo = a.codigo_carretera AND a.gid = a2.gid);

END;
$BODY$ LANGUAGE plpgsql;
