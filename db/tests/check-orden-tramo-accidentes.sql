SELECT plan(COUNT(*)::int) FROM inventario.accidentes;

SELECT ok(tramo IS NOT NULL, 'tramo is NOT NULL, gid = '||gid)
    FROM inventario.accidentes;

SELECT * FROM finish();
