-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.readjust_tramos(
       the_carretera_code TEXT,
       the_pk_inicial float,
       the_pk_final float,
       the_offset float
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
