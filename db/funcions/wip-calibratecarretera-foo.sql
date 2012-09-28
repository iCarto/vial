
SELECT inventario.create_pks();

SELECT AddGeometryColumn('public', 'pks', 'the_geom', '25829', 'POINTM', 3);
SELECT inventario.update_geom_point_all('public', 'pks');

-- obter PK inicial & final
SELECT ST_Line_Locate_Point(ST_GeometryN(the_geom, 1), ST_PointN(the_geom, 1783)) * pk_final
FROM inventario.carreteras WHERE numero = '0101';
