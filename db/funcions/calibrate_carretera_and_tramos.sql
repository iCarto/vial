-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.calibrate_carretera_and_tramos() RETURNS trigger AS $BODY$
BEGIN

       IF (TG_OP = 'INSERT') THEN

           -- calibrate carretera
           PERFORM inventario.calibrate_carretera(NEW.numero);

           -- re-referencing tramos
           PERFORM inventario.update_geom_line_on_pk_carretera_change(
                        'inventario', 'tipo_pavimento', NEW.numero);
           PERFORM inventario.update_geom_line_on_pk_carretera_change(
                        'inventario', 'ancho_plataforma', NEW.numero);
           PERFORM inventario.update_geom_line_on_pk_carretera_change(
                        'inventario', 'cotas', NEW.numero);
           RETURN NEW;

        ELSE IF ((TG_OP = 'UPDATE')
                AND ((NEW.pk_inicial <> OLD.pk_inicial) OR (NEW.pk_final <> OLD.pk_final))) THEN

           -- calibrate carretera
           PERFORM inventario.calibrate_carretera(NEW.numero);

           -- re-referencing tramos
           PERFORM inventario.update_geom_line_on_pk_carretera_change(
                        'inventario', 'tipo_pavimento', NEW.numero);
           PERFORM inventario.update_geom_line_on_pk_carretera_change(
                        'inventario', 'ancho_plataforma', NEW.numero);
           PERFORM inventario.update_geom_line_on_pk_carretera_change(
                        'inventario', 'cotas', NEW.numero);
           RETURN NEW;

        END IF;
        END IF;

        RETURN NULL; --result is ignored since this is an AFTER trigger

END;
$BODY$ LANGUAGE plpgsql;
