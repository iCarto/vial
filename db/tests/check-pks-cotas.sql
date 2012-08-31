BEGIN;
SELECT plan(COUNT(*)::int) FROM inventario.cotas;

SELECT ok(pk_inicial <= pk_final, 'PK inicial <= PK final, gid = '||gid)
    FROM inventario.cotas;

SELECT * FROM finish();
ROLLBACK;
