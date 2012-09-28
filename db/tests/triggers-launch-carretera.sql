BEGIN;

SELECT plan(10);

SELECT trigger_is('inventario',
                  'carreteras',
                  'calibrate_carretera_and_tramos',
                  'inventario',
                  'calibrate_carretera_and_tramos');

SELECT trigger_is('inventario',
                  'carreteras_lugo',
                  'update_longitud',
                  'inventario',
                  'update_longitud');

SELECT trigger_is('inventario',
                  'carreteras_lugo',
                  'mirror_carreteras_lugo',
                  'inventario',
                  'mirror_carreteras_lugo');

SELECT trigger_is('inventario',
                  'carretera_municipio',
                  'update_longitud_carretera_municipio',
                  'inventario',
                  'update_longitud_carretera_municipio');

SELECT trigger_is('inventario',
                  'carretera_municipio',
                  'update_pks_1000',
                  'inventario',
                  'update_pks_1000');

INSERT INTO inventario.carreteras_lugo
       (numero, pk_inicial, pk_final, the_geom)
       VALUES('6666', 0, 100, ST_GeomFromText('MULTILINESTRING((-70.729212 42.373848,-70.67569 42.375098))', 25829));
SELECT is(GeometryType(the_geom), 'MULTILINESTRINGM', 'carreteras - geom calibrated on INSERT')
       FROM inventario.carreteras
       WHERE numero = '6666';

INSERT INTO inventario.carreteras_lugo
       (numero, pk_inicial, pk_final, the_geom)
       VALUES('9999', 2, 23.5, ST_GeomFromText('MULTILINESTRING((-70.729212 42.373848,-70.67569 42.375098))', 25829));
SELECT is(longitud, '21500', 'carreteras - longitud calculated on INSERT')
       FROM inventario.carreteras
       WHERE numero = '9999';

INSERT INTO inventario.carretera_municipio
       (codigo_carretera, codigo_municipio, orden_tramo, pk_inicial_tramo, pk_final_tramo)
       VALUES('9999', '27001', 'A', 3, 9.65);
SELECT is(longitud_tramo, '6650', 'carretera_municipio - longitud calculated on INSERT')
       FROM inventario.carretera_municipio
       WHERE codigo_carretera = '9999' AND codigo_municipio = '27001' AND orden_tramo = 'A';
SELECT is(COUNT(*), '7', 'PKS on tramo')
       FROM public.pks
       WHERE codigo_carretera = '9999' AND codigo_municipio = '27001';

UPDATE inventario.carretera_municipio
       SET pk_inicial_tramo = 6
       WHERE codigo_carretera = '9999'
             AND codigo_municipio = '27001';
SELECT is(COUNT(*), '4', 'PKS on tramo')
       FROM public.pks
       WHERE codigo_carretera = '9999' AND codigo_municipio = '27001';

ROLLBACK;
