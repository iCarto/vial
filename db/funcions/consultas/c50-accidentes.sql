SELECT m.codigo AS "CM", \
       m.nombre AS "Municipio", \
       i.codigo_carretera AS "Código (LU-P)", \
       i.tramo AS "Tramo", \
       c.denominacion AS "Denominación", \
       COALESCE(i.poblacion, '') As "Población", \
       COALESCE(i.valor, '') As "Tipo accidente", \
       i.fecha AS "Fecha", \
       i.pk AS "PK", \
       COALESCE(i.id_accidente, '') AS "ID accidente", \
       COALESCE(i.sentido, '') AS "Sentido", \
       COALESCE(i.luminosidad, '') AS "Luminosidad", \
       COALESCE(i.superficie, '') AS "Superficie", \
       COALESCE(i.visibilidad_restringida_por, '') As "Visibilidad restringida por", \
       COALESCE(i.factores_atmosfericos, '') AS "Factores atmosféricos", \
       COALESCE(i.mediana, '') AS "Mediana entre calzadas", \
       COALESCE(i.barrera_seguridad, '') AS "Barrera seguridad", \
       COALESCE(i.paneles_direccionales, '') AS "Paneles direccionales", \
       COALESCE(i.hitos_arista, '') AS "Hitos arista", \
       COALESCE(i.capta_faros, '') AS "Capta faros", \
       COALESCE(i.prioridad_regulada_por, '') AS "Prioridad regulada por", \
       COALESCE(i.circulacion, '') AS "Circulación", \
       COALESCE(i.circulacion_medidas_especiales, '') AS "Circulación medidas especiales", \
       COALESCE(i.interseccion_con, '') AS "Intersección con", \
       COALESCE(i.tipo_interseccion, '') AS "Tipo intersección", \
       COALESCE(i.acondicionamiento_interseccion, '') AS "Acondicionamiento intersección", \
       COALESCE(i.fuera_interseccion, '') AS "Fuera intersección", \
       COALESCE(i.tipo_accidente, '') AS "Tipo accidente", \
       i.muertos AS "Muertos", \
       i.heridos_graves AS "Heridos graves", \
       i.heridos_leves AS "Heridos leves", \
       i.vehiculos_implicados AS "Vehículos implicados" \
FROM inventario.accidentes i, \
     inventario.carreteras c, \
     inventario.municipio_codigo m \
WHERE i.codigo_carretera = c.numero \
      AND i.codigo_municipio = m.codigo \
      [[WHERE]] \
ORDER BY to_number(m.codigo, '99'), \
         m.nombre, \
         i.codigo_carretera, \
         i.tramo, \
         i.fecha DESC, \
         i.pk;
