SELECT ca.valor_minimo||' - '||ca.valor_maximo AS "Rango IMD", \
       SUM(cm.longitud_tramo)/1000 AS "Longitud (km)", \
       SUM(i.valor)/COUNT(*) AS "IMD media", \
       (SUM(cm.longitud_tramo)/1000)*(SUM(i.valor)/COUNT(*)) AS "Veh - KM", \
       (SUM(cm.longitud_tramo)/1000)*(SUM(i.valor)/COUNT(*))*365 AS "Veh - KM - Año" \
FROM consultas.aforos AS ca, \
     inventario.aforos i, \
     inventario.carretera_municipio cm \
WHERE i.codigo_carretera = cm.codigo_carretera \
      AND i.codigo_municipio = cm.codigo_municipio \
      AND i.tramo = cm.orden_tramo \
      AND i.valor > ca.valor_minimo \
      AND i.valor < ca.valor_maximo \
      [[WHERE]] \
GROUP BY ca.valor_minimo, \
         ca.valor_maximo \
ORDER BY ca.valor_minimo, \
         ca.valor_maximo;
