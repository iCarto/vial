SELECT mun_cod.nombre AS "Municipio", \
       actuaciones.autorizacion_fecha_autorizacion AS "Fecha autorización", \
       actuaciones.autorizacion_tipo AS "Tipo", \
       sum(autorizacion_importe_tasas) AS "Importe tasas", \
       sum(autorizacion_importe_aval) AS "Importe aval" \
FROM inventario.actuaciones actuaciones, \
     inventario.actuacion_municipio act_mun, \
     inventario.municipio_codigo mun_cod \
WHERE actuaciones.codigo_actuacion = act_mun.codigo_actuacion AND \
      act_mun.codigo_municipio = mun_cod.codigo AND \
      actuaciones.tipo = 'Autorización' \
[[WHERE]] \
GROUP BY mun_cod.nombre, actuaciones.autorizacion_fecha_autorizacion, actuaciones.autorizacion_tipo;
