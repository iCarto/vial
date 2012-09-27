DROP SCHEMA IF EXISTS inventario CASCADE;
DELETE FROM public.geometry_columns WHERE f_table_schema = 'inventario';
CREATE SCHEMA inventario;
