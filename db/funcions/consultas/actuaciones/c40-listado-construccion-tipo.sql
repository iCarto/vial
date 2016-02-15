SELECT mun_cod.nombre AS "Municipio", \
       actuaciones.codigo_actuacion AS "Código actuación", \
       actuaciones.codigo_carretera AS "Código (LU-P)", \
       actuaciones.pk_inicial AS "PK inicial", \
       actuaciones.pk_final AS "PK final", \
       actuaciones.codigo_actuacion AS "Código actuación", \
       COALESCE(actuaciones.construccion_tipo, '') AS "Tipo", \
       COALESCE(actuaciones.construccion_titulo, '') AS "Título", \
       COALESCE(actuaciones.construccion_descripcion, '') AS "Descripción", \
       COALESCE(actuaciones.construccion_contratista_nombre, '') AS "Contratista", \
       actuaciones.construccion_importe_proyecto AS "Iporte proyecto", \
       actuaciones.construccion_importe_adjudicacion AS "Importe adjudicación", \
       actuaciones.construccion_fecha_consignacion_aprobacion AS "Fecha aprobación presupuesto", \
       COALESCE(actuaciones.construccion_observaciones, '') AS "Observaciones" \
FROM inventario.actuaciones actuaciones, \
     inventario.actuacion_municipio act_mun, \
     inventario.municipio_codigo mun_cod, \
     inventario.carreteras carreteras \
WHERE actuaciones.codigo_actuacion = act_mun.codigo_actuacion AND \
      act_mun.codigo_municipio = mun_cod.codigo AND \
      actuaciones.codigo_carretera = carreteras.numero AND \
      actuaciones.tipo = 'Construcción' \
[[WHERE]];
