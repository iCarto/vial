-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.update_pks_1000_function(
       the_carretera_code varchar(4)) RETURNS void AS $BODY$
BEGIN

        EXECUTE 'DELETE FROM inventario.pks_1000
                 WHERE codigo_carretera = '''||the_carretera_code||''';';

        EXECUTE 'INSERT INTO inventario.pks_1000(
                 SELECT nextval(''inventario.pks_1000_gid_seq''),
                        '''||the_carretera_code||''',
                        '''',
                        generate_series(
                                CAST(round(min(pk_inicial_tramo)) AS int) + 1,
                                CAST(round(max(pk_final_tramo)) AS int),
                                1) AS pk
                 FROM inventario.carretera_municipio
                 WHERE codigo_carretera='''||the_carretera_code||'''
                       GROUP BY codigo_carretera
                       ORDER BY codigo_carretera
                       );';

        EXECUTE 'UPDATE inventario.pks_1000 AS b
                 SET codigo_municipio = a.codigo_municipio
                 FROM inventario.carretera_municipio a
                 WHERE b.codigo_carretera = '''||the_carretera_code||'''
                       AND b.pk > a.pk_inicial_tramo
                       AND b.pk < a.pk_final_tramo;';

END;
$BODY$ LANGUAGE plpgsql;
