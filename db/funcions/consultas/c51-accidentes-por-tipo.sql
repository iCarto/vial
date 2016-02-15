SELECT m.codigo AS "CM", \
       m.nombre AS "Municipio", \
       i.codigo_carretera AS "Código (LU-P)", \
       i.orden_tramo AS "Tramo", \
       c.denominacion AS "Denominación", \
       COALESCE(p.tipo_accidente, '') AS "Tipo de accidente", \
       COUNT(p.valor) AS "Número de accidentes" \
FROM inventario.carretera_municipio i, \
     inventario.accidentes p, \
     inventario.carreteras c, \
     inventario.municipio_codigo m \
WHERE i.codigo_carretera = p.codigo_carretera \
      AND i.codigo_municipio = p.codigo_municipio \
      AND i.orden_tramo = p.tramo \
      AND i.codigo_carretera = c.numero \
      AND i.codigo_municipio = m.codigo \
      [[WHERE]] \
GROUP BY m.codigo, \
         m.nombre, \
         i.codigo_carretera, \
         i.codigo_municipio, \
         i.orden_tramo, \
         c.denominacion, \
         p.tipo_accidente \
ORDER BY to_number(m.codigo, '99'), \
         m.nombre, \
         i.codigo_carretera, \
         i.orden_tramo, \
         p.tipo_accidente \
;
