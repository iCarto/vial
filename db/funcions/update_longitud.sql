-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.update_longitud() RETURNS trigger AS $BODY$
BEGIN

        IF (TG_OP = 'UPDATE') THEN
           NEW.longitud = CAST(((NEW.pk_final - NEW.pk_inicial)*1000) AS int) ;
           RETURN NEW;

        ELSE IF (TG_OP = 'INSERT') THEN
           NEW.longitud = CAST(((NEW.pk_final - NEW.pk_inicial)*1000) AS int);
           RETURN NEW;

        END IF;
        END IF;

        RETURN NEW;

END;
$BODY$ LANGUAGE plpgsql;
