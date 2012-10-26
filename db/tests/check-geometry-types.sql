BEGIN;

SELECT plan(14);

-- accidentes
SELECT ok(type = 'POINTM', 'Accidentes geom type OK')
FROM public.geometry_columns
WHERE f_table_schema = 'inventario'
      AND f_table_name = 'accidentes';

SELECT ok(coord_dimension = 3, 'Accidentes coord_dimension OK')
FROM public.geometry_columns
WHERE f_table_schema = 'inventario'
      AND f_table_name = 'accidentes';

-- actuaciones
SELECT ok(type = 'MULTILINESTRINGM', 'Actuaciones geom type OK')
FROM public.geometry_columns
WHERE f_table_schema = 'inventario'
      AND f_table_name = 'actuaciones';

SELECT ok(coord_dimension = 3, 'Actuaciones coord_dimension OK')
FROM public.geometry_columns
WHERE f_table_schema = 'inventario'
      AND f_table_name = 'actuaciones';

-- aforos
SELECT ok(type = 'POINTM', 'Aforos geom type OK')
FROM public.geometry_columns
WHERE f_table_schema = 'inventario'
      AND f_table_name = 'aforos';

SELECT ok(coord_dimension = 3, 'Aforos coord_dimension OK')
FROM public.geometry_columns
WHERE f_table_schema = 'inventario'
      AND f_table_name = 'aforos';

-- ancho_plataforma
SELECT ok(type = 'MULTILINESTRINGM', 'Ancho plataforma geom type OK')
FROM public.geometry_columns
WHERE f_table_schema = 'inventario'
      AND f_table_name = 'ancho_plataforma';

SELECT ok(coord_dimension = 3, 'Ancho plataforma coord_dimension OK')
FROM public.geometry_columns
WHERE f_table_schema = 'inventario'
      AND f_table_name = 'ancho_plataforma';

-- carreteras
SELECT ok(type = 'MULTILINESTRINGM', 'Carreteras geom type OK')
FROM public.geometry_columns
WHERE f_table_schema = 'inventario'
      AND f_table_name = 'carreteras';

SELECT ok(coord_dimension = 3, 'Carreteras coord_dimension OK')
FROM public.geometry_columns
WHERE f_table_schema = 'inventario'
      AND f_table_name = 'carreteras';

-- cotas
SELECT ok(type = 'MULTILINESTRINGM', 'Cotas geom type OK')
FROM public.geometry_columns
WHERE f_table_schema = 'inventario'
      AND f_table_name = 'cotas';

SELECT ok(coord_dimension = 3, 'Cotas coord_dimension OK')
FROM public.geometry_columns
WHERE f_table_schema = 'inventario'
      AND f_table_name = 'cotas';

-- tipo pavimento
SELECT ok(type = 'MULTILINESTRINGM', 'Tipo pavimento geom type OK')
FROM public.geometry_columns
WHERE f_table_schema = 'inventario'
      AND f_table_name = 'tipo_pavimento';

SELECT ok(coord_dimension = 3, 'Tipo pavimento coord_dimension OK')
FROM public.geometry_columns
WHERE f_table_schema = 'inventario'
      AND f_table_name = 'tipo_pavimento';

ROLLBACK;
