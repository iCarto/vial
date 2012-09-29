BEGIN;

SELECT plan(2);

SELECT cmp_ok('2', '>=', COUNT(DISTINCT(estado)))
       FROM inventario.rampas;

SELECT cmp_ok('2', '>=', COUNT(DISTINCT(estado)))
       FROM inventario.variantes;

ROLLBACK;
