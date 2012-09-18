SELECT a.codigo_carretera, \
       m.nombre, \
       a.orden, \
       a.valor, \
       a.pk, \
       a.fecha \
 FROM inventario.aforos AS a, \
      inventario.municipio_codigo AS m \
 WHERE a.codigo_municipio = m.codigo \
       AND a.valor > 250 \
 ORDER BY a.codigo_carretera, a.orden, a.fecha;
