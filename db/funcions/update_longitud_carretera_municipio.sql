-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.update_longitud_carretera_municipio() RETURNS trigger AS $BODY$
BEGIN

        IF (TG_OP = 'UPDATE') THEN
           NEW.longitud_tramo = (NEW.pk_final_tramo - NEW.pk_inicial_tramo)*1000;
           RETURN NEW;

        ELSE IF (TG_OP = 'INSERT') THEN
           NEW.longitud_tramo = (NEW.pk_final_tramo - NEW.pk_inicial_tramo)*1000;
           RETURN NEW;

        END IF;
        END IF;

        RETURN NEW;

END;
$BODY$ LANGUAGE plpgsql;
