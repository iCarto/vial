SELECT distinct(actuaciones.codigo_carretera), count(actuaciones.tipo = 'Accidente') \
FROM inventario.actuaciones actuaciones, \
     inventario.carretera_municipio car_mun, \
     inventario.actuacion_municipio act_mun \
WHERE actuaciones.codigo_carretera = car_mun.codigo_carretera AND \
      actuaciones.codigo_actuacion = act_mun.codigo_actuacion \
[[WHERE]] \
GROUP BY actuaciones.codigo_carretera, car_mun.codigo_municipio \
HAVING count(actuaciones.tipo = 'Accidente') > 10;

