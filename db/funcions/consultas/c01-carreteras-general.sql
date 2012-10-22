WITH trv AS ( \
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
       trv.codigo_carretera AS "Código (LU-P)", \
       trv.orden_tramo AS "Tramo", \
       c.denominacion AS "Denominación", \
       c.origen_via AS "Origen vía", \
       c.final_via AS "Final vía", \
       trv.longitud_tronco + trv.longitud_rampas + trv.longitud_variantes AS "Longitud tramo", \
       trv_c.longitud_tronco + trv_c.longitud_rampas + trv_c.longitud_variantes AS "Longitud total", \
       CASE \
       WHEN c.intermunicipal \
            AND trv.longitud_rampas <> 0 \
            AND trv.longitud_variantes <> 0 THEN \
                COALESCE(c.observaciones, '')|| \
                ' TRONCO ('||trv.longitud_tronco||'),'|| \
                ' RAMPAS ('||trv.longitud_rampas||'),'|| \
                ' TRAMOS VIEJOS ('||trv.longitud_variantes||');'|| \
                ' INTERMUNICIPAL '|| trv_m_array.longitud_por_municipio \
       WHEN c.intermunicipal \
            AND trv.longitud_rampas <> 0 THEN \
                COALESCE(c.observaciones, '')|| \
                ' TRONCO ('||trv.longitud_tronco||'),'|| \
                ' RAMPAS ('||trv.longitud_rampas||');'|| \
                ' INTERMUNICIPAL '|| trv_m_array.longitud_por_municipio \
       WHEN c.intermunicipal \
            AND trv.longitud_variantes <> 0 THEN \
                COALESCE(c.observaciones, '')|| \
                ' TRONCO ('||trv.longitud_tronco||'),'|| \
                ' TRAMOS VIEJOS ('||trv.longitud_variantes||');'|| \
                ' INTERMUNICIPAL '|| trv_m_array.longitud_por_municipio \
       WHEN trv.longitud_rampas <> 0 THEN \
                COALESCE(c.observaciones, '')|| \
                ' TRONCO ('||trv.longitud_tronco||'),'|| \
                ' RAMPAS ('||trv.longitud_rampas||')' \
       WHEN trv.longitud_variantes <> 0 THEN \
                COALESCE(c.observaciones, '')|| \
                ' TRONCO('||trv.longitud_tronco||'),'|| \
                ' TRAMOS VIEJOS ('||trv.longitud_variantes||')' \
       WHEN c.intermunicipal THEN \
                COALESCE(c.observaciones, '')|| \
                ' INTERMUNICIPAL '||trv_m_array.longitud_por_municipio \
       ELSE COALESCE(c.observaciones, '') \
       END AS "Observaciones" \
FROM inventario.carreteras AS c, \
     inventario.municipio_codigo AS m, \
     trv, \
     (SELECT codigo_carretera, \
             SUM(longitud_tronco) AS longitud_tronco, \
             SUM(longitud_rampas) AS longitud_rampas, \
             SUM(longitud_variantes) AS longitud_variantes \
      FROM trv \
      GROUP BY codigo_carretera \
      ORDER BY codigo_carretera) AS trv_c, \
     (SELECT codigo_carretera, \
             array_to_string(array_agg(municipio_nombre||' ('||municipio_longitud||')'), \
                                       ', ') AS longitud_por_municipio \
      FROM (SELECT m.nombre AS municipio_nombre, \
                   trv.codigo_carretera, \
                   COALESCE(SUM(trv.longitud_tronco),0) \
                   + COALESCE(SUM(trv.longitud_rampas),0) \
                   + COALESCE(SUM(trv.longitud_variantes),0) AS municipio_longitud \
            FROM inventario.municipio_codigo AS m, \
                 trv \
            WHERE trv.codigo_municipio = m.codigo \
            GROUP BY m.nombre, trv.codigo_carretera \
            ORDER BY m.nombre, trv.codigo_carretera) AS trv_m \
      GROUP BY codigo_carretera) AS trv_m_array \
WHERE trv.codigo_carretera = c.numero \
      AND trv.codigo_municipio = m.codigo \
      AND trv.codigo_carretera = trv_c.codigo_carretera \
      AND trv.codigo_carretera = trv_m_array.codigo_carretera \
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
ORDER BY m.nombre, trv.codigo_carretera, trv.orden_tramo;
