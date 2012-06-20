-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.trigger_update_geom_ancho_plataforma() RETURNS trigger AS $BODY$
BEGIN

        IF(TG_OP = 'UPDATE') THEN
           IF ((NEW.origentram <> OLD.origentram) OR (NEW.finaltramo <> OLD.finaltramo)) THEN

                  SELECT ST_Locate_Between_Measures(c.the_geom, a.origentram, a.finaltramo) AS new_geom
                         INTO NEW.the_geom
                         FROM inventario.rede_carreteras AS c, inventario.ancho_plataforma AS a
                         WHERE c.codigo=a.carretera AND a.gid = NEW.gid;
                  RETURN NEW;

            END IF;

        ELSE IF (TG_OP = 'INSERT') THEN

                  SELECT ST_Locate_Between_Measures(c.the_geom, a.origentram, a.finaltramo) AS new_geom
                         INTO NEW.the_geom
                         FROM inventario.rede_carreteras AS c, inventario.ancho_plataforma AS a
                         WHERE c.codigo=a.carretera AND a.gid = NEW.gid;
                   RETURN NEW;

        END IF;
        END IF;
        RETURN NEW;

END;
$BODY$ LANGUAGE plpgsql;
