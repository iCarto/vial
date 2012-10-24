WITH c AS (SELECT codigo_carretera, \
                   codigo_municipio, \
                   tramo, \
                   MIN(valor) AS cota_minima, \
                   MAX(valor) AS cota_maxima \
            FROM inventario.cotas \
            GROUP BY codigo_carretera, codigo_municipio, tramo) \
SELECT ms.nombre AS "Municipio", \
       c.codigo_carretera AS "Código (LU-P)", \
       c.tramo AS "Tramo", \
       cs.denominacion AS "Denominación", \
       c.cota_minima AS "Cota mínima", \
       c.cota_maxima AS "Cota máxima" \
FROM c, \
     inventario.carreteras AS cs, \
     inventario.municipio_codigo AS ms \
WHERE c.codigo_municipio = ms.codigo \
      AND c.codigo_carretera = cs.numero \
      [[WHERE]] \
ORDER BY ms.nombre, \
         c.codigo_carretera, \
         c.tramo;
