SELECT i.codigo_carretera AS "Código (LU-P)", \
       i.orden_tramo AS "Tramo", \
       m.nombre as "Municipio", \
       c.denominacion AS "Denominación", \
       c.origen_via AS "Origen vía", \
       c.final_via AS "Final vía", \
       i.longitud_tramo AS "Longitud tramo", \
       c.longitud AS "Longitud carretera", \
       aux.len_por_concello AS "Longitud por municipio" \
 FROM inventario.carretera_municipio AS i, \
      inventario.carreteras AS c, \
      inventario.municipio_codigo AS m, \
      (SELECT codigo_carretera, array_to_string(array_agg(nombre || ' (' || longitud_tramo ||')'), ', ') AS len_por_concello \
              FROM inventario.carretera_municipio, inventario.municipio_codigo \
              WHERE codigo = codigo_municipio \
              GROUP BY codigo_carretera) AS aux \
 WHERE aux.codigo_carretera = i.codigo_carretera \
       AND c.numero = i.codigo_carretera \
       AND m.codigo = i.codigo_municipio \
 GROUP BY "Código (LU-P)", \
          "Tramo", \
          "Municipio", \
          "Denominación", \
          "Origen vía", \
          "Final vía", \
          "Longitud tramo", \
          "Longitud carretera", \
          "Longitud por municipio" \
 ORDER BY i.codigo_carretera, i.orden_tramo;
