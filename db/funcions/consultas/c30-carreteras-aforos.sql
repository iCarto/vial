SELECT m.nombre AS "Municipio", \
       i.codigo_carretera AS "Código (LU-P)", \
       i.orden_tramo AS "Tramo", \
       c.denominacion AS "Denominación", \
       p.valor AS "Aforo", \
       p.pk "PK", \
       ap.valor "Ancho (m)", \
       tp.valor "Tipo pavimento", \
       p.fecha "Fecha" \
 FROM inventario.carretera_municipio AS i, \
      inventario.carreteras AS c, \
      inventario.municipio_codigo AS m, \
      inventario.aforos AS p, \
      inventario.ancho_plataforma AS ap, \
      inventario.tipo_pavimento AS tp \
 WHERE i.codigo_carretera = c.numero \
       AND i.codigo_municipio = m.codigo \
       AND i.codigo_carretera = p.codigo_carretera \
       AND i.codigo_municipio = p.codigo_municipio \
       AND i.orden_tramo = p.tramo \
       AND i.codigo_carretera = tp.codigo_carretera \
       AND i.codigo_municipio = tp.codigo_municipio \
       AND i.orden_tramo = tp.tramo \
       AND tp.pk_inicial <= p.pk \
       AND tp.pk_final >= p.pk \
       AND i.codigo_carretera = ap.codigo_carretera \
       AND i.codigo_municipio = ap.codigo_municipio \
       AND i.orden_tramo = ap.tramo \
       AND ap.pk_inicial <= p.pk \
       AND ap.pk_final >= p.pk \
       [[WHERE]] \
 ORDER BY m.nombre, i.codigo_carretera, i.orden_tramo, p.pk, p.fecha;
