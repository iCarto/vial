BEGIN;
SELECT plan(COUNT(*)::int) FROM inventario.accidentes;

SELECT isnt(NULL, pk, 'PK NOT NULL')
       FROM inventario.accidentes;

SELECT * FROM finish();
ROLLBACK;
