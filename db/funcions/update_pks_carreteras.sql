-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.update_pks_carreteras() RETURNS trigger AS $BODY$
BEGIN

        IF(TG_OP = 'INSERT') THEN

                 EXECUTE 'UPDATE inventario.carreteras_lugo
                          SET pk_inicial = (SELECT min(pk_inicial_tramo)
                                            FROM inventario.carretera_municipio
                                            WHERE codigo_carretera = numero
                                                  AND codigo_carretera = '''||NEW.codigo_carretera||'''),
                              pk_final = (SELECT max(pk_final_tramo)
                                          FROM inventario.carretera_municipio
                                          WHERE codigo_carretera = numero
                                                AND codigo_carretera = '''||NEW.codigo_carretera||''')
                          WHERE numero='''||NEW.codigo_carretera||''';';

                 RETURN NEW;

        ELSE IF ((TG_OP = 'UPDATE')
             AND ((NEW.pk_inicial_tramo <> OLD.pk_inicial_tramo)
                   OR (NEW.pk_final_tramo <> OLD.pk_final_tramo))) THEN

                 EXECUTE 'UPDATE inventario.carreteras_lugo
                          SET pk_inicial = (SELECT min(pk_inicial_tramo)
                                            FROM inventario.carretera_municipio
                                            WHERE codigo_carretera = numero
                                                  AND codigo_carretera = '''||NEW.codigo_carretera||'''),
                              pk_final = (SELECT max(pk_final_tramo)
                                          FROM inventario.carretera_municipio
                                          WHERE codigo_carretera = numero
                                                AND codigo_carretera = '''||NEW.codigo_carretera||''')
                          WHERE numero='''||NEW.codigo_carretera||''';';

             RETURN NEW;

        END IF;
        END IF;

        RETURN NULL;

END;
$BODY$ LANGUAGE plpgsql;
