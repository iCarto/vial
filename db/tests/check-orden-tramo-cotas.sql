SELECT plan(COUNT(*)::int) FROM inventario.cotas;

SELECT ok(tramo IS NOT NULL, 'tramo is NOT NULL, gid = '||gid)
    FROM inventario.cotas;

SELECT * FROM finish();
