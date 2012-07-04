-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.update_geom_tipo_pavimento() RETURNS trigger AS $BODY$
BEGIN

        IF (TG_OP = 'UPDATE') THEN
           IF ((NEW.origenpavi <> OLD.origenpavi) OR (NEW.finalpavim <> OLD.finalpavim)) THEN

                  SELECT ST_CollectionExtract(ST_Locate_Between_Measures(c.the_geom, NEW.origenpavi, NEW.finalpavim), 2)
                         INTO NEW.the_geom
                         FROM inventario.rede_carreteras AS c
                         WHERE c.codigo=NEW.carretera;
                  RETURN NEW;
           END IF;

        ELSE IF (TG_OP = 'INSERT') THEN

                  SELECT ST_CollectionExtract(ST_Locate_Between_Measures(c.the_geom, NEW.origenpavi, NEW.finalpavim), 2)
                         INTO NEW.the_geom
                         FROM inventario.rede_carreteras AS c
                         WHERE c.codigo=NEW.carretera;
                  RETURN NEW;

        END IF;
        END IF;

        RETURN NEW;

END;
$BODY$ LANGUAGE plpgsql;
