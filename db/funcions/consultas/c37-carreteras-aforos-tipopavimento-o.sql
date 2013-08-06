WITH p AS ( \
     SELECT codigo_carretera, \
            codigo_municipio, \
            tramo, \
            MAX(fecha) AS fecha_ultimo_aforo \
     FROM inventario.aforos \
     GROUP BY codigo_carretera, \
              codigo_municipio, \
              tramo \
     ORDER BY codigo_carretera, \
              codigo_municipio, \
              tramo \
) \
SELECT m.codigo AS "CM", \
       m.nombre AS "Municipio", \
       i.codigo_carretera AS "Código (LU-P)", \
       i.tramo AS "Tramo", \
       c.denominacion AS "Denominación", \
       i.valor AS "Aforos", \
       i.pk AS "PK", \
       ap.valor AS "Ancho (m)", \
       tp.valor AS "Tipo pavimento", \
       i.fecha AS "Fecha" \
FROM inventario.aforos AS i, \
     inventario.municipio_codigo AS m, \
     inventario.carreteras AS c, \
     inventario.ancho_plataforma AS ap, \
     inventario.tipo_pavimento AS tp, \
     p \
WHERE i.codigo_carretera = p.codigo_carretera \
      AND i.codigo_municipio = p.codigo_municipio \
      AND i.tramo = p.tramo \
      AND i.fecha = p.fecha_ultimo_aforo \
      AND i.codigo_municipio = m.codigo \
      AND i.codigo_carretera = c.numero \
      AND i.codigo_carretera = ap.codigo_carretera \
      AND i.codigo_municipio = ap.codigo_municipio \
      AND i.tramo = ap.tramo \
      AND i.pk >= ap.pk_inicial \
      AND i.pk <= ap.pk_final \
      AND i.codigo_carretera = tp.codigo_carretera \
      AND i.codigo_municipio = tp.codigo_municipio \
      AND i.tramo = tp.tramo \
      AND i.pk >= tp.pk_inicial \
      AND i.pk <= tp.pk_final \
      AND tp.valor = 'O' \
      [[WHERE]] \
ORDER BY to_number(m.codigo, '99'), \
         m.nombre, \
         i.codigo_carretera, \
         i.tramo, \
         i.pk, \
         i.fecha \
;
