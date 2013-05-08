SELECT mun_cod.nombre AS "Municipio", \
       actuaciones.codigo_carretera AS "Código (LU-P)", \
       actuaciones.pk_inicial AS "PK inicial", \
       actuaciones.pk_final AS "PK final", \
       actuaciones.codigo_actuacion AS "Código actuación", \
       actuaciones.inventario_tipo AS "Tipo", \
       actuaciones.inventario_margen AS "Margen", \
       actuaciones.inventario_ano AS "Año", \
       actuaciones.inventario_utm_x AS "UTM X", \
       actuaciones.inventario_utm_y AS "UTM Y", \
       actuaciones.inventario_utm_Z AS "UTM Z", \
       actuaciones.inventario_observaciones AS "Observaciones" \
FROM inventario.actuaciones actuaciones, \
     inventario.actuacion_municipio act_mun, \
     inventario.municipio_codigo mun_cod, \
     inventario.carreteras carreteras \
WHERE actuaciones.codigo_actuacion = act_mun.codigo_actuacion AND \
      act_mun.codigo_municipio = mun_cod.codigo AND \
      actuaciones.codigo_carretera = carreteras.numero AND \
      actuaciones.tipo = 'Inventario' \
[[WHERE]];