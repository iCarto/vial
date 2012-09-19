SELECT l.codigo_carretera AS "Código (LU-P)", \
       l.orden_tramo AS "Tramo", \
       m.nombre AS "Municipio", \
       c.denominacion AS "Denominación", \
       p.valor AS "Ancho (m)", \
       p.pk_inicial "PK inicial", \
       p.pk_final "PK final", \
       p.longitud "Longitud" \
 FROM inventario.carretera_municipio AS l, \
      inventario.carreteras AS c, \
      inventario.municipio_codigo AS m, \
      inventario.ancho_plataforma AS p \
 WHERE l.codigo_carretera = c.numero \
       AND l.codigo_municipio = m.codigo \
       AND l.codigo_carretera = p.codigo_carretera \
       AND l.codigo_municipio = p.codigo_municipio \
       AND l.orden_tramo = p.tramo \
       AND p.valor < 5 \
       [[WHERE]] \
 ORDER BY l.codigo_carretera, orden_tramo, p.pk_inicial, p.pk_final;
