SELECT m.codigo AS "CM", \
       m.nombre AS "Municipio", \
       i.codigo_carretera AS "Código (LU-P)", \
       i.tramo AS "Tramo", \
       c.denominacion AS "Denominación", \
       i.orden_variante AS "Código", \
       i.orden_variante_tramo AS "Orden", \
       i.origen_via_pk AS "PK origen vía", \
       i.final_via_pk AS "PK final vía", \
       i.longitud AS "Longitud", \
       i.estado AS "Estado", \
       i.ancho_plataforma AS "Ancho plataforma", \
       i.tipo_pavimento AS "Tipo pavimento", \
       i.observaciones AS "Observaciones" \
FROM inventario.variantes i, \
     inventario.carreteras c, \
     inventario.municipio_codigo m \
WHERE i.codigo_carretera = c.numero \
      AND i.codigo_municipio = m.codigo \
      [[WHERE]] \
ORDER BY to_number(m.codigo, '99'), \
         m.nombre, \
         i.codigo_carretera, \
         i.tramo, \
         i.orden_variante, \
         i.orden_variante_tramo, \
         i.pk_inicial, \
         i.pk_final \
;
