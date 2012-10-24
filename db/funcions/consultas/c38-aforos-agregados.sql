SELECT ca.valor_minimo||' - '||ca.valor_maximo AS "Rango IMD", \
       SUM(longitud_tramo)/1000 AS "Longitud (km)", \
       SUM(valor)/COUNT(*) AS "IMD media", \
       (SUM(longitud_tramo)/1000)*(SUM(valor)/COUNT(*)) AS "Veh - KM", \
       (SUM(longitud_tramo)/1000)*(SUM(valor)/COUNT(*))*365 AS "Veh - KM - Año" \
FROM consultas.aforos AS ca, \
     inventario.carretera_municipio i, \
     inventario.aforos p \
WHERE p.codigo_carretera = i.codigo_carretera \
      AND p.codigo_municipio = i.codigo_municipio \
      AND p.tramo = i.orden_tramo \
      AND p.valor > ca.valor_minimo \
      AND p.valor < ca.valor_maximo \
      [[WHERE]] \
GROUP BY ca.valor_minimo, \
         ca.valor_maximo \
ORDER BY ca.valor_minimo, \
         ca.valor_maximo;
