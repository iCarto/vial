BEGIN;

SELECT plan(20);

UPDATE inventario.carreteras_lugo
       SET numero = '9999'
       WHERE numero = '2801';

-- Foreign Key will update carretera in mirror table
SELECT is(COUNT(*), '0', 'Carreteras with code 2801')
       FROM inventario.carreteras
       WHERE numero = '2801';
SELECT is(COUNT(*), '1', 'Carreteras with code 9999')
       FROM inventario.carreteras
       WHERE numero = '9999';

-- FK will change codigo_carretera
SELECT is(COUNT(*), '0', 'accidentes with code 2801')
       FROM inventario.accidentes
       WHERE codigo_carretera = '2801';
SELECT cmp_ok('0', '<', COUNT(*), 'accidentes with code 9999')
       FROM inventario.accidentes
       WHERE codigo_carretera = '9999';

SELECT is(COUNT(*), '0', 'actuaciones with code 2801')
       FROM inventario.actuaciones
       WHERE codigo_carretera = '2801';
SELECT cmp_ok('0', '<', COUNT(*), 'actuaciones with code 9999')
       FROM inventario.actuaciones
       WHERE codigo_carretera = '9999';

SELECT is(COUNT(*), '0', 'aforos with code 2801')
       FROM inventario.aforos
       WHERE codigo_carretera = '2801';
SELECT cmp_ok('0', '<', COUNT(*), 'aforos with code 9999')
       FROM inventario.aforos
       WHERE codigo_carretera = '9999';

SELECT is(COUNT(*), '0', 'ancho_plataforma with code 2801')
       FROM inventario.ancho_plataforma
       WHERE codigo_carretera = '2801';
SELECT cmp_ok('0', '<', COUNT(*), 'ancho_plataforma with code 9999')
       FROM inventario.ancho_plataforma
       WHERE codigo_carretera = '9999';

SELECT is(COUNT(*), '0', 'cotas with code 2801')
       FROM inventario.cotas
       WHERE codigo_carretera = '2801';
SELECT cmp_ok('0', '<', COUNT(*), 'cotas with code 9999')
       FROM inventario.cotas
       WHERE codigo_carretera = '9999';

SELECT is(COUNT(*), '0', 'pks 1000 with code 2801')
       FROM inventario.pks_1000
       WHERE codigo_carretera = '2801';
SELECT cmp_ok('0', '<', COUNT(*), 'pks 1000 with code 9999')
       FROM inventario.pks_1000
       WHERE codigo_carretera = '9999';

SELECT is(COUNT(*), '0', 'tipo_pavimento with code 2801')
       FROM inventario.tipo_pavimento
       WHERE codigo_carretera = '2801';
SELECT cmp_ok('0', '<', COUNT(*), 'tipo_pavimento with code 9999')
       FROM inventario.tipo_pavimento
       WHERE codigo_carretera = '9999';

-- Also for variantes & rampas
SELECT is(COUNT(*), '0', 'variantes with code 2801')
       FROM inventario.variantes
       WHERE codigo_carretera = '2801';
SELECT cmp_ok('0', '<', COUNT(*), 'variantes with code 9999')
       FROM inventario.variantes
       WHERE codigo_carretera = '9999';

SELECT is(COUNT(*), '0', 'rampas with code 2801')
       FROM inventario.rampas
       WHERE codigo_carretera = '2801';
SELECT cmp_ok('0', '<', COUNT(*), 'rampas with code 9999')
       FROM inventario.rampas
       WHERE codigo_carretera = '9999';

ROLLBACK;
