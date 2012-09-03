-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.update_geom_point_on_pk_change() RETURNS trigger AS $BODY$
BEGIN

        IF (TG_OP = 'UPDATE') THEN
           IF (NEW.pk <> OLD.pk) THEN

                  SELECT (ST_Dump(the_geom)).geom
                  INTO NEW.the_geom
                  FROM (SELECT ST_Locate_Along_Measure(c.the_geom, NEW.pk) AS the_geom
                        FROM inventario.carreteras AS c
                        WHERE c.numero = NEW.codigo_carretera) AS a;

                  RETURN NEW;
           END IF;

        ELSE IF (TG_OP = 'INSERT') THEN

                  SELECT (ST_Dump(the_geom)).geom
                  INTO NEW.the_geom
                  FROM (SELECT ST_Locate_Along_Measure(c.the_geom, NEW.pk) AS the_geom
                        FROM inventario.carreteras AS c
                        WHERE c.numero = NEW.codigo_carretera) AS a;

                  RETURN NEW;

        END IF;
        END IF;

        RETURN NEW;

END;
$BODY$ LANGUAGE plpgsql;
