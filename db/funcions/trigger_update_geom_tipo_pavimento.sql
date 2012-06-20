-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.trigger_update_geom_tipo_pavimento() RETURNS trigger AS $BODY$
BEGIN

        IF (TG_OP = 'UPDATE') THEN
           IF ((NEW.origenpavi <> OLD.origenpavi) OR (NEW.finalpavim <> OLD.finalpavim)) THEN

                  SELECT ST_Locate_Between_Measures(c.the_geom, p.origenpavi, p.finalpavim) AS new_geom
                         INTO NEW.the_geom
                         FROM inventario.rede_carreteras AS c, inventario.tipo_pavimento AS p
                         WHERE c.codigo=p.carretera AND p.gid = NEW.gid;
                  RETURN NEW;

           END IF;

        ELSE IF (TG_OP = 'INSERT') THEN

                  SELECT ST_Locate_Between_Measures(c.the_geom, p.origenpavi, p.finalpavim) AS new_geom
                         INTO NEW.the_geom
                         FROM inventario.rede_carreteras AS c, inventario.tipo_pavimento AS p
                         WHERE c.codigo=p.carretera AND p.gid = NEW.gid;
                  RETURN NEW;

        END IF;
        END IF;
        RETURN NEW;

END;
$BODY$ LANGUAGE plpgsql;
