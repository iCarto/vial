DROP SCHEMA IF EXISTS info_base CASCADE;
DELETE FROM public.geometry_columns WHERE f_table_schema = 'info_base';