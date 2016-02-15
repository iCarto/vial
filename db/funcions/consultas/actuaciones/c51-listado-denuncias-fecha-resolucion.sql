SELECT mun_cod.nombre AS "Municipio", \
       actuaciones.codigo_carretera AS "Código (LU-P)", \
       actuaciones.pk_inicial AS "PK inicial", \
       actuaciones.pk_final AS "PK final", \
       actuaciones.codigo_actuacion AS "Código actuación", \
       COALESCE(actuaciones.denuncia_tipo, '') AS "Tipo", \
       COALESCE(actuaciones.denuncia_denunciante, '') AS "Denunciante", \
       actuaciones.denuncia_fecha_denuncia AS "Fecha denuncia", \
       actuaciones.denuncia_fecha_resolucion AS "Fecha resolución", \
       actuaciones.denuncia_importe_sancion AS "Importe", \
       COALESCE(actuaciones.denuncia_observaciones, '') AS "Observaciones" \
FROM inventario.actuaciones actuaciones, \
     inventario.actuacion_municipio act_mun, \
     inventario.municipio_codigo mun_cod, \
     inventario.carreteras carreteras \
WHERE actuaciones.codigo_actuacion = act_mun.codigo_actuacion AND \
      act_mun.codigo_municipio = mun_cod.codigo AND \
      actuaciones.codigo_carretera = carreteras.numero AND \
      actuaciones.tipo = 'Denuncia' \
[[WHERE]];
