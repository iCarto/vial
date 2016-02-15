SELECT mun_cod.nombre AS "Municipio", \
       actuaciones.codigo_carretera AS "Código (LU-P)", \
       actuaciones.pk_inicial AS "PK inicial", \
       actuaciones.pk_final AS "PK final", \
       actuaciones.codigo_actuacion AS "Código actuación", \
       COALESCE(actuaciones.travesia_nucleo_poblacion, '') AS "Núcleo población", \
       COALESCE(actuaciones.travesia_tipo_suelo, '') AS "Tipo suelo", \
       actuaciones.travesia_ancho_calzada AS "Ancho calzada", \
       actuaciones.travesia_ancho_acera_izquierda AS "Ancho acera izqda", \
       actuaciones.travesia_ancho_acera_derecha AS "Ancho acera dcha", \
       actuaciones.travesia_ancho_arcen_izquierda AS "Ancho arcén izqdo", \
       actuaciones.travesia_ancho_arcen_derecha AS "Ancho arcén dcho", \
       COALESCE(actuaciones.travesia_observaciones, '') AS "Observaciones" \
FROM inventario.actuaciones actuaciones, \
     inventario.actuacion_municipio act_mun, \
     inventario.municipio_codigo mun_cod, \
     inventario.carreteras carreteras \
WHERE actuaciones.codigo_actuacion = act_mun.codigo_actuacion AND \
      act_mun.codigo_municipio = mun_cod.codigo AND \
      actuaciones.codigo_carretera = carreteras.numero AND \
      actuaciones.tipo = 'Travesía' \
[[WHERE]];
