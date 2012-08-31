SELECT plan(COUNT(*)::int) FROM inventario.ancho_plataforma;

SELECT ok(tramo IS NOT NULL, 'tramo is NOT NULL, gid = '||gid)
    FROM inventario.ancho_plataforma;

SELECT * FROM finish();
