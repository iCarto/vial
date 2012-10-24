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
       SELECT 'C20',
              'Tipo de firme',
              sql_string,
              'SI',
              'Tipo de firme',
              ''
       FROM public.consultas_sql
       WHERE id=3;

INSERT INTO consultas.consultas
       SELECT 'C21',
              'Tipo de firme = MB',
              sql_string,
              'SI',
              'Tipo de firme = MB',
              ''
       FROM public.consultas_sql
       WHERE id=4;

INSERT INTO consultas.consultas
       SELECT 'C22',
              'Tipo de firme = TS',
              sql_string,
              'SI',
              'Tipo de firme = TS',
              ''
       FROM public.consultas_sql
       WHERE id=5;

INSERT INTO consultas.consultas
       SELECT 'C30',
              'Aforos',
              sql_string,
              'SI',
              'Aforos',
              ''
       FROM public.consultas_sql
       WHERE id=6;

INSERT INTO consultas.consultas
       SELECT 'C40',
              'Cotas',
              sql_string,
              'SI',
              'Cotas',
              ''
       FROM public.consultas_sql
       WHERE id=7;

INSERT INTO consultas.consultas
       SELECT 'C02',
              'Carreteras por categoría',
              sql_string,
              'NO',
              'Carreteras por categoría',
              ''
       FROM public.consultas_sql
       WHERE id=8;

INSERT INTO consultas.consultas
       SELECT 'C31',
              'Aforos, donde ancho < 5',
              sql_string,
              'SI',
              'Aforos, donde ancho < 5',
              ''
       FROM public.consultas_sql
       WHERE id=9;

INSERT INTO consultas.consultas
       SELECT 'C32',
              'Aforos, donde ancho >= 5 y >7',
              sql_string,
              'SI',
              'Aforos, donde ancho >= 5 y >7',
              ''
       FROM public.consultas_sql
       WHERE id=10;

INSERT INTO consultas.consultas
       SELECT 'C33',
              'Aforos, donde ancho > 7',
              sql_string,
              'SI',
              'Aforos, donde ancho > 7',
              ''
       FROM public.consultas_sql
       WHERE id=11;

INSERT INTO consultas.consultas
       SELECT 'C34',
              'Aforos, donde tipo pavimento = TS',
              sql_string,
              'SI',
              'Aforos, donde tipo pavimento = TS',
              ''
       FROM public.consultas_sql
       WHERE id=12;

INSERT INTO consultas.consultas
       SELECT 'C35',
              'Aforos, donde tipo pavimento = MB',
              sql_string,
              'SI',
              'Aforos, donde tipo pavimento = MB',
              ''
       FROM public.consultas_sql
       WHERE id=13;

INSERT INTO consultas.consultas
       SELECT 'C36',
              'Aforos, donde tipo pavimento = H',
              sql_string,
              'SI',
              'Aforos, donde tipo pavimento = H',
              ''
       FROM public.consultas_sql
       WHERE id=14;

INSERT INTO consultas.consultas
       SELECT 'C37',
              'Aforos, donde tipo pavimento = O',
              sql_string,
              'SI',
              'Aforos, donde tipo pavimento = O',
              ''
       FROM public.consultas_sql
       WHERE id=15;

INSERT INTO consultas.consultas
       SELECT 'C41',
              'Cotas mínimas / máximas por carretera',
              sql_string,
              'SI',
              'Cotas mínimas / máximas por carretera',
              ''
       FROM public.consultas_sql
       WHERE id=16;

DROP TABLE IF EXISTS public.consultas_sql;
