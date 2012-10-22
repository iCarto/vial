WITH i AS ( \
     SELECT cmt.codigo_carretera, \
            cmt.codigo_municipio, \
            cmt.orden_tramo, \
            COALESCE(SUM(cmt.longitud_tramo),0) AS longitud_tronco, \
            COALESCE(SUM(r.longitud),0) AS longitud_rampas, \
            COALESCE(SUM(v.longitud),0) AS longitud_variantes \
     FROM inventario.carretera_municipio cmt \
          LEFT OUTER JOIN inventario.rampas r ON \
               cmt.codigo_municipio = r.codigo_municipio \
               AND cmt.codigo_carretera = r.codigo_carretera \
               AND cmt.orden_tramo = r.tramo \
          LEFT OUTER JOIN inventario.variantes v ON \
               cmt.codigo_municipio = v.codigo_municipio \
               AND cmt.codigo_carretera = v.codigo_carretera \
               AND cmt.orden_tramo = v.tramo \
     GROUP BY cmt.codigo_municipio, \
              cmt.codigo_carretera, \
              cmt.orden_tramo \
     ORDER BY cmt.codigo_carretera, \
              cmt.codigo_municipio, \
              cmt.orden_tramo) \
SELECT m.nombre as "Municipio", \
       i.codigo_carretera AS "Código (LU-P)", \
       i.orden_tramo AS "Tramo", \
       c.denominacion AS "Denominación", \
       c.origen_via AS "Origen vía", \
       c.final_via AS "Final vía", \
       i.longitud_tronco + i.longitud_rampas + i.longitud_variantes AS "Longitud tramo", \
       trv_c.longitud_tronco + trv_c.longitud_rampas + trv_c.longitud_variantes AS "Longitud total", \
       CASE \
       WHEN c.intermunicipal \
            AND i.longitud_rampas <> 0 \
            AND i.longitud_variantes <> 0 THEN \
                COALESCE(c.observaciones, '')|| \
                ' TRONCO ('||i.longitud_tronco||'),'|| \
                ' RAMPAS ('||i.longitud_rampas||'),'|| \
                ' TRAMOS VIEJOS ('||i.longitud_variantes||');'|| \
                ' INTERMUNICIPAL '|| trv_m_array.longitud_por_municipio \
       WHEN c.intermunicipal \
            AND i.longitud_rampas <> 0 THEN \
                COALESCE(c.observaciones, '')|| \
                ' TRONCO ('||i.longitud_tronco||'),'|| \
                ' RAMPAS ('||i.longitud_rampas||');'|| \
                ' INTERMUNICIPAL '|| trv_m_array.longitud_por_municipio \
       WHEN c.intermunicipal \
            AND i.longitud_variantes <> 0 THEN \
                COALESCE(c.observaciones, '')|| \
                ' TRONCO ('||i.longitud_tronco||'),'|| \
                ' TRAMOS VIEJOS ('||i.longitud_variantes||');'|| \
                ' INTERMUNICIPAL '|| trv_m_array.longitud_por_municipio \
       WHEN i.longitud_rampas <> 0 THEN \
                COALESCE(c.observaciones, '')|| \
                ' TRONCO ('||i.longitud_tronco||'),'|| \
                ' RAMPAS ('||i.longitud_rampas||')' \
       WHEN i.longitud_variantes <> 0 THEN \
                COALESCE(c.observaciones, '')|| \
                ' TRONCO('||i.longitud_tronco||'),'|| \
                ' TRAMOS VIEJOS ('||i.longitud_variantes||')' \
       WHEN c.intermunicipal THEN \
                COALESCE(c.observaciones, '')|| \
                ' INTERMUNICIPAL '||trv_m_array.longitud_por_municipio \
       ELSE COALESCE(c.observaciones, '') \
       END AS "Observaciones" \
FROM inventario.carreteras AS c, \
     inventario.municipio_codigo AS m, \
     i, \
     (SELECT codigo_carretera, \
             SUM(longitud_tronco) AS longitud_tronco, \
             SUM(longitud_rampas) AS longitud_rampas, \
             SUM(longitud_variantes) AS longitud_variantes \
      FROM i \
      GROUP BY codigo_carretera \
      ORDER BY codigo_carretera) AS trv_c, \
     (SELECT codigo_carretera, \
             array_to_string(array_agg(municipio_nombre||' ('||municipio_longitud||')'), \
                                       ', ') AS longitud_por_municipio \
      FROM (SELECT m.nombre AS municipio_nombre, \
                   i.codigo_carretera, \
                   COALESCE(SUM(i.longitud_tronco),0) \
                   + COALESCE(SUM(i.longitud_rampas),0) \
                   + COALESCE(SUM(i.longitud_variantes),0) AS municipio_longitud \
            FROM inventario.municipio_codigo AS m, \
                 i \
            WHERE i.codigo_municipio = m.codigo \
            GROUP BY m.nombre, i.codigo_carretera \
            ORDER BY m.nombre, i.codigo_carretera) AS trv_m \
      GROUP BY codigo_carretera) AS trv_m_array \
WHERE i.codigo_carretera = c.numero \
      AND i.codigo_municipio = m.codigo \
      AND i.codigo_carretera = trv_c.codigo_carretera \
      AND i.codigo_carretera = trv_m_array.codigo_carretera \
      [[WHERE]] \
GROUP BY "Municipio", \
         "Código (LU-P)", \
         "Tramo", \
         "Denominación", \
         "Origen vía", \
         "Final vía", \
         "Longitud tramo", \
         "Longitud total", \
         "Observaciones" \
ORDER BY m.nombre, i.codigo_carretera, i.orden_tramo;
