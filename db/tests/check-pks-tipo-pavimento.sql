BEGIN;
SELECT plan(COUNT(*)::int) FROM inventario.tipo_pavimento;

SELECT ok(pk_inicial <= pk_final, 'PK inicial <= PK final, gid = '||gid)
    FROM inventario.tipo_pavimento;

SELECT * FROM finish();
ROLLBACK;
