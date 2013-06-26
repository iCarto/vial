-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.mirror_carreteras_lugo() RETURNS trigger AS $BODY$
BEGIN

        IF (TG_OP = 'UPDATE') THEN
             UPDATE inventario.carreteras
                    SET codigo = NEW.codigo,
                        numero = NEW.numero,
                        intermunicipal = NEW.intermunicipal,
                        denominacion = NEW.denominacion,
                        pk_inicial = NEW.pk_inicial,
                        pk_final = NEW.pk_final,
                        origen_via = NEW.origen_via,
                        origen_via_pk = NEW.origen_via_pk,
                        final_via = NEW.final_via,
                        final_via_pk = NEW.final_via_pk,
                        categoria = NEW.categoria,
                        topografia = NEW.topografia,
                        tipo_suelo = NEW.tipo_suelo,
                        longitud = NEW.longitud,
                        longitud_antigua = NEW.longitud_antigua,
                        observaciones = NEW.observaciones,
                        the_geom = (SELECT ST_AddMeasure(NEW.the_geom,
                                              NEW.pk_inicial,
                                              NEW.pk_final))
                     WHERE numero = NEW.numero;
                  RETURN NEW;

        ELSE IF (TG_OP = 'INSERT') THEN
             INSERT INTO inventario.carreteras(
                         codigo,
                         numero,
                         intermunicipal,
                         denominacion,
                         pk_inicial,
                         pk_final,
                         origen_via,
                         origen_via_pk,
                         final_via,
                         final_via_pk,
                         categoria,
                         topografia,
                         tipo_suelo,
                         longitud,
                         longitud_antigua,
                         observaciones,
                         the_geom)
                    VALUES(
                         NEW.codigo,
                         NEW.numero,
                         NEW.intermunicipal,
                         NEW.denominacion,
                         NEW.pk_inicial,
                         NEW.pk_final,
                         NEW.origen_via,
                         NEW.origen_via_pk,
                         NEW.final_via,
                         NEW.final_via_pk,
                         NEW.categoria,
                         NEW.topografia,
                         NEW.tipo_suelo,
                         NEW.longitud,
                         NEW.longitud_antigua,
                         NEW.observaciones,
                         (SELECT ST_AddMeasure(NEW.the_geom,
                                              NEW.pk_inicial,
                                              NEW.pk_final)));

                   RETURN NEW;

        END IF;
        END IF;

        RETURN NEW;

END;
$BODY$ LANGUAGE plpgsql;
