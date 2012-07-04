BEGIN;
SELECT plan(COUNT(*)::int) FROM inventario.ancho_plataforma;

SELECT ok(origentram <= finaltramo, 'PK inicial <= PK final: '||gid)
    FROM inventario.ancho_plataforma;

SELECT * FROM finish();
ROLLBACK;
