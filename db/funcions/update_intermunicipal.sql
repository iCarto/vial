-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.update_intermunicipal() RETURNS trigger AS $BODY$
BEGIN

        IF(TG_OP = 'DELETE') THEN

                 EXECUTE 'UPDATE inventario.carreteras_lugo
                          SET intermunicipal = (SELECT COUNT(*) > 1
                                                FROM inventario.carretera_municipio
                                                WHERE codigo_carretera = '''||OLD.codigo_carretera||'''
                                                GROUP BY codigo_carretera)
                          WHERE numero = '''||OLD.codigo_carretera||''';';

                 RETURN OLD;

        ELSE IF(TG_OP = 'INSERT') THEN

                 EXECUTE 'UPDATE inventario.carreteras_lugo
                          SET intermunicipal = (SELECT COUNT(*) > 1
                                                FROM inventario.carretera_municipio
                                                WHERE codigo_carretera = '''||NEW.codigo_carretera||'''
                                                GROUP BY codigo_carretera)
                          WHERE numero = '''||NEW.codigo_carretera||''';';

                 RETURN NEW;

        END IF;
        END IF;

        RETURN NULL;

END;
$BODY$ LANGUAGE plpgsql;
