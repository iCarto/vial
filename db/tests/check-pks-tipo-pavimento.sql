BEGIN;
SELECT plan(COUNT(*)::int) FROM inventario.tipo_pavimento;

SELECT ok(origenpavi <= finalpavim, 'PK inicial <= PK final: '||gid)
    FROM inventario.tipo_pavimento;

SELECT * FROM finish();
ROLLBACK;
