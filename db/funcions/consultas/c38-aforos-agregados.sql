SELECT ca.valor_minimo||' - '||ca.valor_maximo AS "Rango IMD", \
       SUM(longitud_tramo)/1000 AS "Longitud (km)", \
       SUM(valor)/COUNT(*) AS "IMD media", \
       (SUM(longitud_tramo)/1000)*(SUM(valor)/COUNT(*)) AS "Veh - KM", \
       (SUM(longitud_tramo)/1000)*(SUM(valor)/COUNT(*))*365 AS "Veh - KM - Año" \
FROM consultas.aforos AS ca, \
     inventario.carretera_municipio i, \
     inventario.aforos a \
WHERE i.codigo_carretera = a.codigo_carretera \
      AND i.codigo_municipio = a.codigo_municipio \
      AND i.orden_tramo = a.tramo \
      AND a.valor > ca.valor_minimo \
      AND a.valor < ca.valor_maximo \
      [[WHERE]] \
GROUP BY ca.valor_minimo, \
         ca.valor_maximo \
ORDER BY ca.valor_minimo, \
         ca.valor_maximo;
