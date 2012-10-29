SELECT m.nombre AS "Municipio", \
       i.codigo_carretera AS "Código (LU-P)", \
       i.tramo AS "Tramo", \
       c.denominacion AS "Denominación", \
       i.poblacion As "Población", \
       i.valor As "ID", \
       i.fecha AS "Fecha", \
       i.pk AS "PK", \
       i.se AS "SE", \
       i.luminosidad AS "Luminosidad", \
       i.superficie AS "Superficie", \
       i.visibilidad_restringida_por As "Visibilidad restringida por", \
       i.factores_atmosfericos AS "Factores atmosféricos", \
       i.mediana AS "Mediana entre calzadas", \
       i.barrera_seguridad AS "Barrera seguridad", \
       i.paneles_direccionales AS "Paneles direccionales", \
       i.hitos_arista AS "Hitos arista", \
       i.capta_faros AS "Capta faros", \
       i.prioridad_regulada_por AS "Prioridad regulada por", \
       i.circulacion AS "Circulación", \
       i.circulacion_medidas_especiales AS "Circulación medidas especiales", \
       i.interseccion_con AS "Intersección con", \
       i.tipo_otros AS "Tipo intersección", \
       i.acondicionamiento_interseccion AS "Acondicionamiento intersección", \
       i.fuera_interseccion AS "Fuera intersección", \
       i.tipo_accidente AS "Tipo accidente", \
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
ORDER BY m.nombre, \
         i.codigo_carretera, \
         i.tramo, \
         i.fecha DESC, \
         i.pk;
