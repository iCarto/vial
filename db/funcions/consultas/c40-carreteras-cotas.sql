SELECT m.codigo AS "CM", \
       m.nombre AS "Municipio", \
       i.codigo_carretera AS "Código (LU-P)", \
       i.orden_tramo AS "Tramo", \
       c.denominacion AS "Denominación", \
       p.valor AS "Cota máxima", \
       p.pk_inicial "PK inicial", \
       p.pk_final "PK final" \
 FROM inventario.carretera_municipio AS i, \
      inventario.carreteras AS c, \
      inventario.municipio_codigo AS m, \
      inventario.cotas AS p \
 WHERE i.codigo_carretera = c.numero \
       AND i.codigo_municipio = m.codigo \
       AND i.codigo_carretera = p.codigo_carretera \
       AND i.codigo_municipio = p.codigo_municipio \
       AND i.orden_tramo = p.tramo \
       [[WHERE]] \
 ORDER BY to_number(m.codigo, '99'), \
          m.nombre, \
          i.codigo_carretera, \
          i.orden_tramo, \
          p.pk_inicial, \
          p.pk_final \
;
