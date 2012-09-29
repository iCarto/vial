BEGIN;
SELECT plan(3*COUNT(*)::int) FROM inventario.tipo_pavimento;

SELECT ok(pk_inicial <= pk_final, 'PK inicial <= PK final, gid = '||gid)
    FROM inventario.tipo_pavimento;

SELECT isnt(NULL, pk_inicial, 'PK inicial NOT NULL')
       FROM inventario.tipo_pavimento;

SELECT isnt(NULL, pk_final, 'PK final NOT NULL')
       FROM inventario.tipo_pavimento;

SELECT * FROM finish();
ROLLBACK;
