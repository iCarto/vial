SELECT mun_cod.nombre AS "Municipio", \
       actuaciones.construccion_fecha_consignacion_aprobacion AS "Fecha aprobación", \
       sum(construccion_importe_proyecto) AS "Importe proyecto", \
       sum(construccion_importe_adjudicacion) AS "Importe adjudicación" \
FROM inventario.actuaciones actuaciones, \
     inventario.actuacion_municipio act_mun, \
     inventario.municipio_codigo mun_cod \
WHERE actuaciones.codigo_actuacion = act_mun.codigo_actuacion AND \
      act_mun.codigo_municipio = mun_cod.codigo AND \
      actuaciones.tipo = 'Construcción' \
[[WHERE]] \
GROUP BY mun_cod.nombre, actuaciones.construccion_fecha_consignacion_aprobacion;