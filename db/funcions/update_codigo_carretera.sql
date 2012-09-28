-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.update_codigo_carretera() RETURNS trigger AS $BODY$
BEGIN

        IF (TG_OP = 'UPDATE') THEN
           NEW.codigo = 'LU-P-'||NEW.numero;
           RETURN NEW;

        ELSE IF (TG_OP = 'INSERT') THEN
           NEW.codigo = 'LU-P-'||NEW.numero;
           RETURN NEW;

        END IF;
        END IF;

        RETURN NEW;

END;
$BODY$ LANGUAGE plpgsql;
