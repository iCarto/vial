SELECT mun_cod.nombre AS "Municipio", \
       actuaciones.codigo_carretera AS "Código (LU-P)", \
       actuaciones.pk_inicial AS "PK inicial", \
       actuaciones.pk_final AS "PK final", \
       actuaciones.codigo_actuacion AS "Código actuación", \
       actuaciones.conservacion_propuesta_tipo AS "Tipo", \
       actuaciones.conservacion_propuesta_titulo AS "Título", \
       actuaciones.conservacion_propuesta_descripcion AS "Descripción", \
       actuaciones.conservacion_propuesta_contratista_nombre AS "Contratista", \
       actuaciones.conservacion_propuesta_contratista_cif AS "CIF", \
       actuaciones.conservacion_propuesta_importe_proyecto AS "Importe proyecto", \
       actuaciones.conservacion_propuesta_fecha_consignacion_aprobacion AS "Fecha aprobación presupuesto", \
       actuaciones.conservacion_propuesta_observaciones AS "Observaciones" \
FROM inventario.actuaciones actuaciones, \
     inventario.actuacion_municipio act_mun, \
     inventario.municipio_codigo mun_cod, \
     inventario.carreteras carreteras \
WHERE actuaciones.codigo_actuacion = act_mun.codigo_actuacion AND \
      act_mun.codigo_municipio = mun_cod.codigo AND \
      actuaciones.codigo_carretera = carreteras.numero AND \
      actuaciones.tipo = 'Cons. (propuesta)' \
[[WHERE]];