BEGIN;
SELECT plan(COUNT(*)::int) FROM inventario.ancho_plataforma;

SELECT ok(pk_inicial <= pk_final, 'PK inicial <= PK final, gid = '||gid)
    FROM inventario.ancho_plataforma;

SELECT * FROM finish();
ROLLBACK;
