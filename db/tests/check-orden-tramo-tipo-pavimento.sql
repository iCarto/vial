BEGIN;
SELECT plan(COUNT(*)::int) FROM inventario.tipo_pavimento;

SELECT ok(tramo IS NOT NULL, 'tramo is NOT NULL, gid = '||gid)
    FROM inventario.tipo_pavimento;

SELECT * FROM finish();

ROLLBACK;
