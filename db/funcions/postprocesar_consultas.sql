INSERT INTO consultas.consultas
       SELECT 'C01',
              'Inventario de carreteras',
              sql_string,
              'SI',
              'Inventario de carreteras',
              ''
       FROM public.consultas_sql
       WHERE id=1;

INSERT INTO consultas.consultas
       SELECT 'C10',
              'Ancho de firme',
              sql_string,
              'SI',
              'Ancho de firme',
              ''
       FROM public.consultas_sql
       WHERE id=2;

INSERT INTO consultas.consultas
       SELECT 'C11',
              'Ancho de firme < 5',
              sql_string,
              'SI',
              'Ancho de firme < 5',
              ''
       FROM public.consultas_sql
       WHERE id=3;

INSERT INTO consultas.consultas
       SELECT 'C12',
              'Ancho de firme >5 y <7',
              sql_string,
              'SI',
              'Ancho de firme >5 y <7',
              ''
       FROM public.consultas_sql
       WHERE id=4;

INSERT INTO consultas.consultas
       SELECT 'C13',
              'Ancho de firme > 7',
              sql_string,
              'SI',
              'Ancho de firme > 7',
              ''
       FROM public.consultas_sql
       WHERE id=5;

INSERT INTO consultas.consultas
       SELECT 'C20',
              'Tipo de firme',
              sql_string,
              'SI',
              'Tipo de firme',
              ''
       FROM public.consultas_sql
       WHERE id=6;

INSERT INTO consultas.consultas
       SELECT 'C21',
              'Tipo de firme = MB',
              sql_string,
              'SI',
              'Tipo de firme = MB',
              ''
       FROM public.consultas_sql
       WHERE id=7;

INSERT INTO consultas.consultas
       SELECT 'C22',
              'Tipo de firme = TS',
              sql_string,
              'SI',
              'Tipo de firme = TS',
              ''
       FROM public.consultas_sql
       WHERE id=8;

INSERT INTO consultas.consultas
       SELECT 'C30',
              'Aforos',
              sql_string,
              'SI',
              'Aforos',
              ''
       FROM public.consultas_sql
       WHERE id=9;

INSERT INTO consultas.consultas
       SELECT 'C31',
              'Aforos > 250',
              sql_string,
              'SI',
              'Aforos > 250',
              ''
       FROM public.consultas_sql
       WHERE id=10;

INSERT INTO consultas.consultas
       SELECT 'C32',
              'Aforos > 500',
              sql_string,
              'SI',
              'Aforos > 500',
              ''
       FROM public.consultas_sql
       WHERE id=11;

INSERT INTO consultas.consultas
       SELECT 'C33',
              'Aforos > 1000',
              sql_string,
              'SI',
              'Aforos > 1000',
              ''
       FROM public.consultas_sql
       WHERE id=12;

INSERT INTO consultas.consultas
       SELECT 'C40',
              'Cotas',
              sql_string,
              'SI',
              'Cotas',
              ''
       FROM public.consultas_sql
       WHERE id=13;

DROP TABLE IF EXISTS public.consultas_sql;
