SELECT mun_cod.nombre AS "Municipio", \
       actuaciones.codigo_actuacion AS "Código actuación", \
       actuaciones.codigo_carretera AS "Código (LU-P)", \
       carreteras.denominacion as "Denominación", \
       actuaciones.pk_inicial AS "PK inicial", \
       actuaciones.pk_final AS "PK_final", \
       actuaciones.conservacion_tipo AS "TIPO", \
       actuaciones.conservacion_descripcion AS "Descripción", \
       actuaciones.conservacion_fecha_inicio AS "Fecha", \
       actuaciones.conservacion_observaciones AS "Observaciones" \
FROM inventario.actuaciones actuaciones, \
     inventario.actuacion_municipio act_mun, \
     inventario.municipio_codigo mun_cod, \
     inventario.carreteras carreteras \
WHERE actuaciones.codigo_actuacion = act_mun.codigo_actuacion AND \
      act_mun.codigo_municipio = mun_cod.codigo AND \
      actuaciones.codigo_carretera = carreteras.numero AND \
      actuaciones.tipo = 'Conservación' \
[[WHERE]];