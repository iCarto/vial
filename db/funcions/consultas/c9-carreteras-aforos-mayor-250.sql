SELECT i.codigo_carretera, \
       m.nombre, \
       i.orden, \
       i.valor, \
       i.pk, \
       i.fecha \
 FROM inventario.aforos AS i, \
      inventario.municipio_codigo AS m \
 WHERE i.codigo_municipio = m.codigo \
       AND i.valor > 250 \
       [[WHERE]] \
 ORDER BY i.codigo_carretera, i.orden, i.fecha;
