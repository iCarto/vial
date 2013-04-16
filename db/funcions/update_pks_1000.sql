-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.update_pks_1000() RETURNS trigger AS $BODY$
BEGIN

        IF(TG_OP = 'DELETE') THEN

                 EXECUTE 'DELETE FROM inventario.pks_1000
                          WHERE codigo_carretera = '''||OLD.codigo_carretera||''';';

                 EXECUTE 'INSERT INTO inventario.pks_1000(
                          SELECT nextval(''inventario.pks_1000_gid_seq''),
                                 '''||OLD.codigo_carretera||''',
                                 '''',
                                 generate_series(
                                        CAST(round(min(pk_inicial_tramo)) AS int) + 1,
                                        CAST(round(max(pk_final_tramo)) AS int),
                                        1) AS pk
                          FROM inventario.carretera_municipio
                          WHERE codigo_carretera='''||OLD.codigo_carretera||'''
                          GROUP BY codigo_carretera
                          ORDER BY codigo_carretera
                 );';

                 EXECUTE 'UPDATE inventario.pks_1000 AS b
                          SET codigo_municipio = a.codigo_municipio
                          FROM inventario.carretera_municipio a
                          WHERE b.codigo_carretera = '''||OLD.codigo_carretera||'''
                                AND b.pk > a.pk_inicial_tramo
                                AND b.pk < a.pk_final_tramo;';

                 RETURN OLD;

        ELSE IF(TG_OP = 'INSERT') THEN

                 EXECUTE 'DELETE FROM inventario.pks_1000
                          WHERE codigo_carretera = '''||NEW.codigo_carretera||''';';

                 EXECUTE 'INSERT INTO inventario.pks_1000(
                          SELECT nextval(''inventario.pks_1000_gid_seq''),
                                 '''||NEW.codigo_carretera||''',
                                 '''',
                                 generate_series(
                                        CAST(round(min(pk_inicial_tramo)) AS int) + 1,
                                        CAST(round(max(pk_final_tramo)) AS int),
                                        1) AS pk
                          FROM inventario.carretera_municipio
                          WHERE codigo_carretera='''||NEW.codigo_carretera||'''
                          GROUP BY codigo_carretera
                          ORDER BY codigo_carretera
                 );';

                 EXECUTE 'UPDATE inventario.pks_1000 AS b
                          SET codigo_municipio = a.codigo_municipio
                          FROM inventario.carretera_municipio a
                          WHERE b.codigo_carretera = '''||NEW.codigo_carretera||'''
                                AND b.pk > a.pk_inicial_tramo
                                AND b.pk < a.pk_final_tramo;';

                 RETURN NEW;

        ELSE IF ((TG_OP = 'UPDATE')
             AND ((NEW.pk_inicial_tramo <> OLD.pk_inicial_tramo)
                   OR (NEW.pk_final_tramo <> OLD.pk_final_tramo))) THEN

                 EXECUTE 'DELETE FROM inventario.pks_1000
                          WHERE codigo_carretera = '''||NEW.codigo_carretera||''';';

                 EXECUTE 'INSERT INTO inventario.pks_1000(
                          SELECT nextval(''inventario.pks_1000_gid_seq''),
                                 '''||NEW.codigo_carretera||''',
                                 '''',
                                 generate_series(
                                        CAST(round(min(pk_inicial_tramo)) AS int) + 1,
                                        CAST(round(max(pk_final_tramo)) AS int),
                                        1) AS pk
                          FROM inventario.carretera_municipio
                          WHERE codigo_carretera='''||NEW.codigo_carretera||'''
                          GROUP BY codigo_carretera
                          ORDER BY codigo_carretera
                 );';

                 EXECUTE 'UPDATE inventario.pks_1000 AS b
                          SET codigo_municipio = a.codigo_municipio
                          FROM inventario.carretera_municipio a
                          WHERE b.codigo_carretera = '''||NEW.codigo_carretera||'''
                                AND b.pk > a.pk_inicial_tramo
                                AND b.pk < a.pk_final_tramo;';

             RETURN NEW;

        END IF;
        END IF;
        END IF;

        RETURN NULL;

END;
$BODY$ LANGUAGE plpgsql;
