WITH i AS ( \
     SELECT cmt.codigo_carretera, \
            cmt.codigo_municipio, \
            cmt.orden_tramo, \
            COALESCE(cmt.longitud_tramo, 0) AS longitud_tronco, \
            COALESCE(rampas.longitud_rampas, 0) AS longitud_rampas, \
            COALESCE(variantes.longitud_variantes, 0) AS longitud_variantes \
     FROM inventario.carretera_municipio cmt, \
          (SELECT cmt_r.codigo_carretera, \
                  cmt_r.codigo_municipio, \
                  cmt_r.orden_tramo, \
                  COALESCE(SUM(CASE \
                                WHEN r.estado = 'USO' THEN \
                                     r.longitud \
                                ELSE 0 \
                                END), \
                           0) AS longitud_rampas \
           FROM inventario.carretera_municipio cmt_r \
                LEFT OUTER JOIN inventario.rampas r ON \
                     cmt_r.codigo_municipio = r.codigo_municipio \
                     AND cmt_r.codigo_carretera = r.codigo_carretera \
                     AND cmt_r.orden_tramo = r.tramo \
           GROUP BY cmt_r.codigo_carretera, \
                    cmt_r.codigo_municipio, \
                    cmt_r.orden_tramo) AS rampas, \
          (SELECT cmt_v.codigo_carretera, \
                  cmt_v.codigo_municipio, \
                  cmt_v.orden_tramo, \
                  COALESCE(SUM(CASE \
                                WHEN v.estado = 'USO' THEN \
                                     v.longitud \
                                ELSE 0 \
                                END), \
                           0) AS longitud_variantes \
           FROM inventario.carretera_municipio cmt_v \
                LEFT OUTER JOIN inventario.variantes v ON \
                     cmt_v.codigo_municipio = v.codigo_municipio \
                     AND cmt_v.codigo_carretera = v.codigo_carretera \
                     AND cmt_v.orden_tramo = v.tramo \
           GROUP BY cmt_v.codigo_carretera, \
                    cmt_v.codigo_municipio, \
                    cmt_v.orden_tramo) AS variantes \
     WHERE cmt.codigo_carretera = rampas.codigo_carretera \
           AND cmt.codigo_municipio = rampas.codigo_municipio \
           AND cmt.orden_tramo = rampas.orden_tramo \
           AND cmt.codigo_municipio = variantes.codigo_municipio \
           AND cmt.orden_tramo = variantes.orden_tramo \
           AND rampas.codigo_carretera = variantes.codigo_carretera \
           AND rampas.codigo_municipio = variantes.codigo_municipio \
           AND rampas.orden_tramo = variantes.orden_tramo \
     ORDER BY cmt.codigo_carretera, \
              cmt.codigo_municipio, \
              cmt.orden_tramo, \
              cmt.longitud_tramo) \
SELECT m.codigo AS "CM", \
       m.nombre as "Municipio", \
       i.codigo_carretera AS "C??digo (LU-P)", \
       i.orden_tramo AS "Tramo", \
       c.denominacion AS "Denominaci??n", \
       COALESCE(c.origen_via, '') AS "Origen v??a", \
       COALESCE(c.final_via, '') AS "Final v??a", \
       i.longitud_tronco + i.longitud_rampas + i.longitud_variantes AS "Longitud tramo", \
       trv_c.longitud_tronco + trv_c.longitud_rampas + trv_c.longitud_variantes AS "Longitud total", \
       CASE \
       WHEN c.intermunicipal \
            AND i.longitud_rampas <> 0 \
            AND i.longitud_variantes <> 0 THEN \
                'TRONCO ('||i.longitud_tronco||'), '|| \
                'RAMPAS ('||i.longitud_rampas||'), '|| \
                'TRAMOS VIEJOS ('||i.longitud_variantes||'); '|| \
                'INTERMUNICIPAL '|| trv_m_array.longitud_por_municipio||'; '|| \
                COALESCE(c.observaciones, '') \
       WHEN c.intermunicipal \
            AND i.longitud_rampas <> 0 THEN \
                'TRONCO ('||i.longitud_tronco||'), '|| \
                'RAMPAS ('||i.longitud_rampas||'); '|| \
                'INTERMUNICIPAL '|| trv_m_array.longitud_por_municipio||'; '|| \
                COALESCE(c.observaciones, '') \
       WHEN c.intermunicipal \
            AND i.longitud_variantes <> 0 THEN \
                'TRONCO ('||i.longitud_tronco||'), '|| \
                'TRAMOS VIEJOS ('||i.longitud_variantes||'); '|| \
                'INTERMUNICIPAL '|| trv_m_array.longitud_por_municipio||'; '|| \
                COALESCE(c.observaciones, '') \
       WHEN c.intermunicipal THEN \
                'INTERMUNICIPAL '||trv_m_array.longitud_por_municipio||'; '|| \
                COALESCE(c.observaciones, '') \
       WHEN i.longitud_rampas <> 0 \
            AND i.longitud_variantes <> 0 THEN \
                'TRONCO ('||i.longitud_tronco||'), '|| \
                'RAMPAS ('||i.longitud_rampas||'), '|| \
                'TRAMOS VIEJOS ('||i.longitud_variantes||'); '|| \
                COALESCE(c.observaciones, '') \
       WHEN i.longitud_rampas <> 0 THEN \
                'TRONCO ('||i.longitud_tronco||'), '|| \
                'RAMPAS ('||i.longitud_rampas||'); '|| \
                COALESCE(c.observaciones, '') \
       WHEN i.longitud_variantes <> 0 THEN \
                'TRONCO('||i.longitud_tronco||'), '|| \
                'TRAMOS VIEJOS ('||i.longitud_variantes||'); '|| \
                COALESCE(c.observaciones, '') \
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
GROUP BY "CM", \
         "Municipio", \
         "C??digo (LU-P)", \
         "Tramo", \
         "Denominaci??n", \
         "Origen v??a", \
         "Final v??a", \
         "Longitud tramo", \
         "Longitud total", \
         "Observaciones" \
ORDER BY to_number(m.codigo, '99'), \
         i.codigo_carretera, \
         i.orden_tramo;
