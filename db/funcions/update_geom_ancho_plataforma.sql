-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.update_geom_ancho_plataforma() RETURNS trigger AS $BODY$
BEGIN

        IF(TG_OP = 'UPDATE') THEN
           IF ((NEW.pk_inicial <> OLD.pk_inicial) OR (NEW.pk_final <> OLD.pk_final)) THEN

                  SELECT ST_CollectionExtract(ST_Locate_Between_Measures(c.the_geom, NEW.pk_inicial, NEW.pk_final), 2)
                         INTO NEW.the_geom
                         FROM inventario.rede_carreteras AS c
                         WHERE c.codigo=NEW.codigo_carretera;
                  RETURN NEW;

            END IF;
        ELSE IF(TG_OP = 'INSERT') THEN

                  SELECT ST_CollectionExtract(ST_Locate_Between_Measures(c.the_geom, NEW.pk_inicial, NEW.pk_final), 2)
                         INTO NEW.the_geom
                         FROM inventario.rede_carreteras AS c
                         WHERE c.codigo=NEW.codigo_carretera;
                  RETURN NEW;

        END IF;
        END IF;

        RETURN NEW;

END;
$BODY$ LANGUAGE plpgsql;
