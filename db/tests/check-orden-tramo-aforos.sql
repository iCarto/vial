SELECT plan(COUNT(*)::int) FROM inventario.aforos;

SELECT ok(tramo IS NOT NULL, 'tramo is NOT NULL, gid = '||gid)
    FROM inventario.aforos;

SELECT * FROM finish();
