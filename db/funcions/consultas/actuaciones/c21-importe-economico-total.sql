SELECT mun_cod.nombre AS "Municipio", \
       actuaciones.conservacion_fecha_aprobacion_junta AS "Fecha aprobación", \
       sum(conservacion_importe_proyecto) AS "Importe proyecto" \
FROM inventario.actuaciones actuaciones, \
     inventario.actuacion_municipio act_mun, \
     inventario.municipio_codigo mun_cod \
WHERE actuaciones.codigo_actuacion = act_mun.codigo_actuacion AND \
      act_mun.codigo_municipio = mun_cod.codigo AND \
      actuaciones.tipo = 'Conservación' \
[[WHERE]] \
GROUP BY mun_cod.nombre, actuaciones.conservacion_fecha_aprobacion_junta;