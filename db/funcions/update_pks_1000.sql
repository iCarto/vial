-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.update_pks_1000() RETURNS trigger AS $BODY$
BEGIN

        IF(TG_OP = 'INSERT') THEN

                 EXECUTE 'DELETE FROM public.pks
                          WHERE codigo_carretera = '''||NEW.codigo_carretera||'''
                                AND codigo_municipio = '''||NEW.codigo_municipio||''';';

                 EXECUTE 'INSERT INTO public.pks(
                          SELECT nextval(''public.pks_gid_seq''),
                                 '''||NEW.codigo_carretera||''',
                                 '''||NEW.codigo_municipio||''',
                                 generate_series(
                                        CAST(round(min(pk_inicial_tramo)) AS int) + 1,
                                        CAST(round(max(pk_final_tramo)) AS int),
                                        1) AS pk
                          FROM inventario.carretera_municipio
                          WHERE codigo_carretera='''||NEW.codigo_carretera||'''
                                AND codigo_municipio = '''||NEW.codigo_municipio||'''
                          GROUP BY codigo_carretera, codigo_municipio
                          ORDER BY codigo_carretera
                 );';

                 RETURN NEW;

        ELSE IF ((TG_OP = 'UPDATE')
             AND ((NEW.pk_inicial_tramo <> OLD.pk_inicial_tramo) OR (NEW.pk_final_tramo <> OLD.pk_final_tramo))) THEN

                 EXECUTE 'DELETE FROM public.pks
                          WHERE codigo_carretera = '''||NEW.codigo_carretera||'''
                                AND codigo_municipio = '''||NEW.codigo_municipio||''';';

                 EXECUTE 'INSERT INTO public.pks(
                          SELECT nextval(''public.pks_gid_seq''),
                                 '''||NEW.codigo_carretera||''',
                                 '''||NEW.codigo_municipio||''',
                                 generate_series(
                                        CAST(round(min(pk_inicial_tramo)) AS int) + 1,
                                        CAST(round(max(pk_final_tramo)) AS int),
                                        1) AS pk
                          FROM inventario.carretera_municipio
                          WHERE codigo_carretera='''||NEW.codigo_carretera||'''
                                AND codigo_municipio = '''||NEW.codigo_municipio||'''
                          GROUP BY codigo_carretera, codigo_municipio
                          ORDER BY codigo_carretera
                 );';

             RETURN NEW;

        END IF;
        END IF;

        RETURN NULL;

END;
$BODY$ LANGUAGE plpgsql;
