SELECT m.nombre AS "Municipio", \
       i.codigo_carretera AS "Código (LU-P)", \
       i.orden_tramo AS "Tramo", \
       c.denominacion AS "Denominación", \
       p.valor AS "Tipo pavimento", \
       p.pk_inicial "PK inicial", \
       p.pk_final "PK final", \
       p.longitud "Longitud" \
 FROM inventario.carretera_municipio AS i, \
      inventario.carreteras AS c, \
      inventario.municipio_codigo AS m, \
      inventario.tipo_pavimento AS p \
 WHERE i.codigo_carretera = c.numero \
       AND i.codigo_municipio = m.codigo \
       AND i.codigo_carretera = p.codigo_carretera \
       AND i.codigo_municipio = p.codigo_municipio \
       AND p.valor = 'TS' \
       [[WHERE]] \
 ORDER BY m.nombre, i.codigo_carretera, orden_tramo;
