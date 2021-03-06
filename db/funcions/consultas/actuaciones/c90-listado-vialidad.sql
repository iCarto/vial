SELECT mun_cod.nombre AS "Municipio", \
       actuaciones.codigo_carretera AS "Código (LU-P)", \
       actuaciones.pk_inicial AS "PK inicial", \
       actuaciones.pk_final AS "PK_final", \
       actuaciones.codigo_actuacion AS "Código actuación", \
       COALESCE(actuaciones.vialidad_codigo, '') AS "Código", \
       COALESCE(actuaciones.vialidad_peticionario, '') AS "Peticionario", \
       COALESCE(actuaciones.vialidad_beneficiario, '') AS "Beneficiario", \
       COALESCE(actuaciones.vialidad_nro_registro, '') AS "Nº registro", \
       actuaciones.vialidad_fecha_peticion AS "Fecha petición", \
       actuaciones.vialidad_fecha_inicio AS "Fecha inicio", \
       actuaciones.vialidad_fecha_fin AS "Fecha fin", \
       COALESCE(actuaciones.vialidad_observaciones, '') AS "Observaciones" \
FROM inventario.actuaciones actuaciones, \
     inventario.actuacion_municipio act_mun, \
     inventario.municipio_codigo mun_cod, \
     inventario.carreteras carreteras \
WHERE actuaciones.codigo_actuacion = act_mun.codigo_actuacion AND \
      act_mun.codigo_municipio = mun_cod.codigo AND \
      actuaciones.codigo_carretera = carreteras.numero AND \
      actuaciones.tipo = 'Vialidad' \
[[WHERE]];
