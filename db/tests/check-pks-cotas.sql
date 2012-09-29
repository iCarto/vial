BEGIN;
SELECT plan(3*COUNT(*)::int) FROM inventario.cotas;

SELECT ok(pk_inicial <= pk_final, 'PK inicial <= PK final, gid = '||gid)
    FROM inventario.cotas;

SELECT isnt(NULL, pk_inicial, 'PK inicial NOT NULL')
       FROM inventario.cotas;

SELECT isnt(NULL, pk_final, 'PK final NOT NULL')
       FROM inventario.cotas;

SELECT * FROM finish();
ROLLBACK;
