BEGIN;

SELECT plan(2);

SELECT trigger_is('inventario',
                  'carreteras',
                  'calibrate_carretera_and_tramos',
                  'inventario',
                  'calibrate_carretera_and_tramos');

INSERT INTO inventario.carreteras
       (numero, pk_inicial, pk_final, the_geom)
       VALUES('6666', 0, 100, ST_GeomFromText('LINESTRING(-70.729212 42.373848,-70.67569 42.375098)', 23029));
SELECT is(GeometryType(the_geom), 'LINESTRINGM', 'carreteras - geom calibrated on INSERT')
       FROM inventario.carreteras
       WHERE numero = '6666';

ROLLBACK;
