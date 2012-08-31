BEGIN;
SELECT plan(COUNT(*)::int) FROM inventario.actuaciones;

SELECT ok(pk_inicial <= pk_final, 'PK inicial <= PK final, gid = '||gid)
    FROM inventario.actuaciones;

SELECT * FROM finish();
ROLLBACK;
