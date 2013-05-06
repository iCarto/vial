SELECT mun_cod.nombre AS "Municipio", \
       sum(conservacion_importe_proyecto) AS "Importe proyecto", \
       sum(construccion_importe_adjudicacion) AS "Importe adjudicación" \
FROM inventario.actuaciones actuaciones, \
     inventario.actuacion_municipio act_mun, \
     inventario.municipio_codigo mun_cod \
WHERE actuaciones.codigo_actuacion = act_mun.codigo_actuacion AND \
      act_mun.codigo_municipio = mun_cod.codigo AND \
      actuaciones.tipo = 'Conservación' \
[[WHERE]];