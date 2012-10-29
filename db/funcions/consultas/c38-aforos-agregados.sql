WITH p AS (SELECT cm.codigo_carretera, \
                  cm.codigo_municipio, \
                  cm.orden_tramo, \
                  cm.longitud_tramo, \
                  MAX(COALESCE(a.fecha, '9999-09-09')) fecha_ultima \
           FROM inventario.carretera_municipio cm LEFT OUTER JOIN inventario.aforos a \
                ON cm.codigo_carretera = a.codigo_carretera \
                   AND cm.codigo_municipio = a.codigo_municipio \
                   AND cm.orden_tramo = a.tramo \
                   [[WHERE]] \
           GROUP BY cm.codigo_carretera, \
                    cm.codigo_municipio, \
                    cm.orden_tramo, \
                    cm.longitud_tramo \
           ORDER BY codigo_carretera, \
                    codigo_municipio, \
                    orden_tramo) \
SELECT ca.valor_minimo ||' - '|| ca.valor_maximo AS "Rango IMD", \
       SUM(summary.longitud_tramo)/1000 AS "Longitud (km)", \
       SUM(summary.valor_maximo)/COUNT(*) AS "IMD media", \
       (SUM(summary.longitud_tramo)/1000)*(SUM(summary.valor_maximo)/COUNT(*)) AS "Veh - KM", \
       (SUM(summary.longitud_tramo)/1000)*(SUM(summary.valor_maximo)/COUNT(*))*365 AS "Veh - KM - Año" \
FROM consultas.aforos ca, \
     (SELECT p.codigo_carretera, \
             p.codigo_municipio, \
             p.orden_tramo, \
             p.longitud_tramo, \
             MAX(COALESCE(a.valor,0)) valor_maximo \
      FROM p LEFT OUTER JOIN inventario.aforos a \
           ON a.codigo_carretera = p.codigo_carretera \
              AND a.codigo_municipio = p.codigo_municipio \
              AND a.tramo = p.orden_tramo \
              AND a.fecha = p.fecha_ultima \
      GROUP BY p.codigo_carretera, \
               p.codigo_municipio, \
               p.orden_tramo, \
               p.longitud_tramo \
      ORDER BY p.codigo_carretera, \
               p.codigo_municipio, \
               p.orden_tramo \
      ) AS summary \
WHERE summary.valor_maximo >= ca.valor_minimo \
      AND summary.valor_maximo <= ca.valor_maximo \
GROUP BY ca.valor_minimo, \
         ca.valor_maximo \
ORDER BY ca.valor_minimo, \
         ca.valor_maximo \
;
