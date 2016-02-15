SELECT mun_cod.nombre AS "Municipio", \
       actuaciones.codigo_carretera AS "Código (LU-P)", \
       carreteras.denominacion AS "Denominación", \
       actuaciones.accidente_fecha AS "Fecha", \
       COALESCE(actuaciones.accidente_tipo, '') AS "Tipo" \
FROM inventario.actuaciones actuaciones, \
     inventario.actuacion_municipio act_mun, \
     inventario.municipio_codigo mun_cod, \
     inventario.carreteras carreteras \
WHERE actuaciones.codigo_actuacion = act_mun.codigo_actuacion AND \
      act_mun.codigo_municipio = mun_cod.codigo AND \
      actuaciones.codigo_carretera = carreteras.numero AND \
      actuaciones.tipo = 'Accidente' \
[[WHERE]] \
GROUP BY mun_cod.nombre, actuaciones.codigo_carretera, carreteras.denominacion, actuaciones.accidente_fecha, actuaciones.accidente_tipo \
HAVING count(actuaciones.tipo = 'Accidente') > 10 \
