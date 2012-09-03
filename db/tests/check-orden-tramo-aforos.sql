SELECT plan(COUNT(*)::int) FROM inventario.aforos;

SELECT ok(orden IS NOT NULL, 'orden is NOT NULL, gid = '||gid)
    FROM inventario.aforos;

SELECT * FROM finish();
