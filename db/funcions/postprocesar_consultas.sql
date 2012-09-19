INSERT INTO consultas.consultas
       SELECT 'C1',
              'Inventario de carreteras',
              sql_string,
              'SI',
              'Inventario de carreteras',
              ''
       FROM public.consultas_sql
       WHERE id=1;

INSERT INTO consultas.consultas
       SELECT 'C2',
              'Ancho de firme',
              sql_string,
              'SI',
              'Ancho de firme',
              ''
       FROM public.consultas_sql
       WHERE id=2;

INSERT INTO consultas.consultas
       SELECT 'C3',
              'Ancho de firme < 5',
              sql_string,
              'SI',
              'Ancho de firme < 5',
              ''
       FROM public.consultas_sql
       WHERE id=3;

INSERT INTO consultas.consultas
       SELECT 'C4',
              'Ancho de firme >5 y <7',
              sql_string,
              'SI',
              'Ancho de firme >5 y <7',
              ''
       FROM public.consultas_sql
       WHERE id=4;

INSERT INTO consultas.consultas
       SELECT 'C5',
              'Ancho de firme > 7',
              sql_string,
              'SI',
              'Ancho de firme > 7',
              ''
       FROM public.consultas_sql
       WHERE id=5;

INSERT INTO consultas.consultas
       SELECT 'C6',
              'Tipo de firme',
              sql_string,
              'SI',
              'Tipo de firme',
              ''
       FROM public.consultas_sql
       WHERE id=6;

INSERT INTO consultas.consultas
       SELECT 'C7',
              'Tipo de firme = MB',
              sql_string,
              'SI',
              'Tipo de firme = MB',
              ''
       FROM public.consultas_sql
       WHERE id=7;

INSERT INTO consultas.consultas
       SELECT 'C8',
              'Carreteras por categoría',
              sql_string,
              'NO',
              'Carreteras por categoría',
              ''
       FROM public.consultas_sql
       WHERE id=8;

INSERT INTO consultas.consultas
       SELECT 'C9',
              'Aforos > 250',
              sql_string,
              'SI',
              'Aforos > 250',
              ''
       FROM public.consultas_sql
       WHERE id=9;

DROP TABLE IF EXISTS public.consultas_sql;
