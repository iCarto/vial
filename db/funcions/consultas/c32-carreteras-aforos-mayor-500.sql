SELECT m.nombre AS "Municipio", \
       i.codigo_carretera AS "Código (LU-P)", \
       i.orden AS "Tramo", \
       i.valor AS "Aforo", \
       i.pk AS "PK", \
       i.fecha AS "Fecha aforo" \
 FROM inventario.aforos AS i, \
      inventario.municipio_codigo AS m \
 WHERE i.codigo_municipio = m.codigo \
       AND i.valor > 500 \
       [[WHERE]] \
 ORDER BY m.nombre, i.codigo_carretera, i.orden, i.fecha;
