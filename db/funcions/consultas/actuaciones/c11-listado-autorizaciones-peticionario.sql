SELECT mun_cod.nombre AS "Municipio", \
       actuaciones.codigo_carretera AS "Código (LU-P)", \
       actuaciones.pk_inicial AS "PK inicial", \
       actuaciones.pk_final AS "PK final", \
       actuaciones.codigo_actuacion AS "Código actuación", \
       COALESCE(actuaciones.autorizacion_tipo, '') AS "Tipo", \
       COALESCE(actuaciones.autorizacion_peticionario, '') AS "Peticionario", \
       COALESCE(actuaciones.autorizacion_beneficiario, '') AS "Beneficiario", \
       actuaciones.autorizacion_fecha_solicitud AS "Fecha solicitud", \
       actuaciones.autorizacion_fecha_autorizacion AS "Fecha autorización", \
       actuaciones.autorizacion_importe_tasas AS "Importe tasas", \
       actuaciones.autorizacion_importe_aval AS "Importe aval", \
       COALESCE(actuaciones.autorizacion_observaciones, '') AS "Observaciones" \
FROM inventario.actuaciones actuaciones, \
     inventario.actuacion_municipio act_mun, \
     inventario.municipio_codigo mun_cod, \
     inventario.carreteras carreteras \
WHERE actuaciones.codigo_actuacion = act_mun.codigo_actuacion AND \
      act_mun.codigo_municipio = mun_cod.codigo AND \
      actuaciones.codigo_carretera = carreteras.numero AND \
      actuaciones.tipo = 'Autorización' \
[[WHERE]];
