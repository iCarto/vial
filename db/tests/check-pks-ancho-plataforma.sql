BEGIN;
SELECT plan(3*COUNT(*)::int) FROM inventario.ancho_plataforma;

SELECT ok(pk_inicial <= pk_final, 'PK inicial <= PK final, gid = '||gid)
    FROM inventario.ancho_plataforma;

SELECT isnt(NULL, pk_inicial, 'PK inicial NOT NULL')
       FROM inventario.ancho_plataforma;

SELECT isnt(NULL, pk_final, 'PK final NOT NULL')
       FROM inventario.ancho_plataforma;

SELECT * FROM finish();
ROLLBACK;
