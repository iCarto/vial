SELECT i.codigo AS "CM", \
       i.nombre AS "Municipio", \
       i.codigo_carretera AS "Código (LU-P)", \
       i.orden_tramo AS "Tramo", \
       i.denominacion AS "Denominación", \
       i.tipo AS "Tipo vía", \
       i.valor AS "Tipo pavimento", \
       i.pk_inicial "PK inicial", \
       i.pk_final "PK final", \
       i.longitud "Longitud" \
FROM(SELECT m.codigo, \
            m.nombre, \
            f.codigo_carretera, \
            p.codigo_municipio, \
            f.orden_tramo, \
            c.denominacion, \
            'Tronco' AS tipo, \
            p.valor, \
            p.pk_inicial, \
            p.pk_final, \
            p.longitud \
     FROM inventario.carretera_municipio AS f, \
          inventario.carreteras AS c, \
          inventario.municipio_codigo AS m, \
          inventario.tipo_pavimento AS p \
     WHERE f.codigo_carretera = c.numero \
           AND f.codigo_municipio = m.codigo \
           AND f.codigo_carretera = p.codigo_carretera \
           AND f.codigo_municipio = p.codigo_municipio \
           AND f.orden_tramo = p.tramo \
 \
     UNION ALL \
 \
     SELECT m.codigo, \
            m.nombre, \
            f.codigo_carretera, \
            r.codigo_municipio, \
            f.orden_tramo, \
            c.denominacion, \
            'Rampa' AS tipo, \
            r.tipo_pavimento AS valor, \
            r.pk_inicial, \
            r.pk_final, \
            r.longitud \
     FROM inventario.carretera_municipio AS f, \
          inventario.carreteras AS c, \
          inventario.municipio_codigo AS m, \
          inventario.rampas AS r \
     WHERE f.codigo_carretera = c.numero \
           AND f.codigo_municipio = m.codigo \
           AND f.codigo_carretera = r.codigo_carretera \
           AND f.codigo_municipio = r.codigo_municipio \
           AND f.orden_tramo = r.tramo \
           AND r.estado = 'USO' \
 \
     UNION ALL \
 \
     SELECT m.codigo, \
            m.nombre, \
            f.codigo_carretera, \
            v.codigo_municipio, \
            f.orden_tramo, \
            c.denominacion, \
            'Tramo antiguo' AS tipo, \
            v.tipo_pavimento AS valor, \
            v.pk_inicial, \
            v.pk_final, \
            v.longitud \
     FROM inventario.carretera_municipio AS f, \
          inventario.carreteras AS c, \
          inventario.municipio_codigo AS m, \
          inventario.variantes AS v \
     WHERE f.codigo_carretera = c.numero \
           AND f.codigo_municipio = m.codigo \
           AND f.codigo_carretera = v.codigo_carretera \
           AND f.codigo_municipio = v.codigo_municipio \
           AND f.orden_tramo = v.tramo \
           AND v.estado = 'USO' \
) AS i \
WHERE i.valor = 'MB' \
      [[WHERE]] \
ORDER BY to_number(i.codigo, '99'), \
         i.nombre, \
         i.codigo_carretera, \
         i.orden_tramo, \
         i.tipo DESC, \
         i.pk_inicial, \
         i.pk_final \
;
