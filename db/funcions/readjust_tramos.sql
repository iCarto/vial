-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.readjust_tramos(
       the_carretera_code TEXT,
       the_pk_inicial float,
       the_pk_final float,
       the_offset float
) RETURNS void AS $BODY$
BEGIN

        PERFORM inventario.update_tramos_in_range(
                'inventario', 'tipo_pavimento', the_carretera_code, the_pk_inicial, the_pk_final, the_offset);
        PERFORM inventario.update_tramos_in_range(
                'inventario', 'ancho_plataforma', the_carretera_code, the_pk_inicial, the_pk_final, the_offset);
        PERFORM inventario.update_tramos_in_range(
                'inventario', 'cotas', the_carretera_code, the_pk_inicial, the_pk_final, the_offset);

END;
$BODY$ LANGUAGE plpgsql;
