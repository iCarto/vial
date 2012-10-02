BEGIN;
SELECT plan(COUNT(*)::int) FROM inventario.aforos;

SELECT isnt(NULL, pk, 'PK NOT NULL')
       FROM inventario.aforos;

SELECT * FROM finish();
ROLLBACK;
