-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.recalculate_caracteristicas(
       the_carretera_code TEXT
) RETURNS void AS $BODY$
BEGIN

        PERFORM inventario.update_geom_line_on_pk_carretera_change(
                'inventario', 'tipo_pavimento', the_carretera_code);
        PERFORM inventario.update_geom_line_on_pk_carretera_change(
                'inventario', 'ancho_plataforma', the_carretera_code);
        PERFORM inventario.update_geom_line_on_pk_carretera_change(
                'inventario', 'cotas', the_carretera_code);

END;
$BODY$ LANGUAGE plpgsql;
