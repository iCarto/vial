-- obter PK inicial & final
SELECT ST_Line_Locate_Point(ST_GeometryN(the_geom, 1), ST_PointN(the_geom, 1783)) * pk_final
FROM inventario.carreteras WHERE numero = '0101';
