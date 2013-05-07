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

INSERT INTO consultas.consultas
       SELECT 'C38',
              'Aforos agregados',
              sql_string,
              'SI',
              'Aforos agregados',
              ''
       FROM public.consultas_sql
       WHERE id=17;

INSERT INTO consultas.consultas
       SELECT 'C03',
              'Inventario de rampas',
              sql_string,
              'SI',
              'Inventario de rampas',
              ''
       FROM public.consultas_sql
       WHERE id=18;

INSERT INTO consultas.consultas
       SELECT 'C04',
              'Inventario de tramos antiguos',
              sql_string,
              'SI',
              'Inventario de tramos antiguos',
              ''
       FROM public.consultas_sql
       WHERE id=19;

INSERT INTO consultas.consultas
       SELECT 'C50',
              'Listado de accidentes',
              sql_string,
              'SI',
              'Listado de accidentes',
              ''
       FROM public.consultas_sql
       WHERE id=20;

INSERT INTO consultas.consultas
       SELECT 'C51',
              'Número de accidentes por tipo',
              sql_string,
              'SI',
              'Número de accidentes por tipo',
              ''
       FROM public.consultas_sql
       WHERE id=21;

INSERT INTO consultas.consultas
       SELECT 'C52',
              'Número de accidentes',
              sql_string,
              'SI',
              'Número de accidentes',
              ''
       FROM public.consultas_sql
       WHERE id=22;

-- CONSULTAS ACTUACIONES
INSERT INTO consultas.consultas_actuaciones
       SELECT 'C01',
              'Listado de accidentes por tipo',
              sql_string,
              'SI',
              'Listado de accidentes por tipo',
              ''
       FROM public.consultas_sql
       WHERE id=23;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C02',
              'Carreteras donde hubo más de 10 accidentes',
              sql_string,
              'SI',
              'Carreteras donde hubo más de 10 accidentes',
              ''
       FROM public.consultas_sql
       WHERE id=24;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C10',
              'Listado de autorizaciones por tipo',
              sql_string,
              'SI',
              'Listado de autorizaciones por tipo',
              ''
       FROM public.consultas_sql
       WHERE id=25;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C11',
              'Listado de autorizaciones por peticionario',
              sql_string,
              'SI',
              'Listado de autorizaciones por peticionario',
              ''
       FROM public.consultas_sql
       WHERE id=26;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C12',
              'Listado de autorizaciones por beneficiario',
              sql_string,
              'SI',
              'Listado de autorizaciones por beneficiario',
              ''
       FROM public.consultas_sql
       WHERE id=27;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C13',
              'Tasas recaudadas por año y/o tipo',
              sql_string,
              'SI',
              'Tasas recaudadas por año y/o tipo',
              ''
       FROM public.consultas_sql
       WHERE id=28;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C20',
              'Listado de conservación por tipo',
              sql_string,
              'SI',
              'Listado de conservación por tipo',
              ''
       FROM public.consultas_sql
       WHERE id=29;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C21',
              'Importe económico total',
              sql_string,
              'SI',
              'Importe económico total',
              ''
       FROM public.consultas_sql
       WHERE id=30;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C22',
              'Listado de conservación por fecha de inicio',
              sql_string,
              'SI',
              'Listado de conservación por fecha de inicio',
              ''
       FROM public.consultas_sql
       WHERE id=31;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C23',
              'Listado de conservación por fecha de fin',
              sql_string,
              'SI',
              'Listado de conservación por fecha de fin',
              ''
       FROM public.consultas_sql
       WHERE id=31;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C30',
              'Listado de conservación por tipo',
              sql_string,
              'SI',
              'Listado de conservación por tipo',
              ''
       FROM public.consultas_sql
       WHERE id=32;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C31',
              'Importe económico total',
              sql_string,
              'SI',
              'Importe económico total',
              ''
       FROM public.consultas_sql
       WHERE id=34;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C40',
              'Listado de construcción por tipo',
              sql_string,
              'SI',
              'Listado de construcción por tipo',
              ''
       FROM public.consultas_sql
       WHERE id=35;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C41',
              'Importe económico total',
              sql_string,
              'SI',
              'Importe económico total',
              ''
       FROM public.consultas_sql
       WHERE id=36;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C50',
              'Listado de denuncias por fecha de denuncia',
              sql_string,
              'SI',
              'Listado de denuncias por fecha de denuncia',
              ''
       FROM public.consultas_sql
       WHERE id=37;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C51',
              'Listado de denuncias por fecha de resolución',
              sql_string,
              'SI',
              'Listado de denuncias por fecha de resolución',
              ''
       FROM public.consultas_sql
       WHERE id=38;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C52',
              'Listado de sanción por importe igual a (X)',
              sql_string,
              'SI',
              'Listado de sanción por importe igual a (X)',
              ''
       FROM public.consultas_sql
       WHERE id=39;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C53',
              'Listado de sanción por importe mayor que (X)',
              sql_string,
              'SI',
              'Listado de sanción por importe mayor que (X)',
              ''
       FROM public.consultas_sql
       WHERE id=40;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C54',
              'Listado de sanción por importe menor que (X)',
              sql_string,
              'SI',
              'Listado de sanción por importe menor que (X)',
              ''
       FROM public.consultas_sql
       WHERE id=41;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C60',
              'Listado de incidencias',
              sql_string,
              'SI',
              'Listado de incidencias',
              ''
       FROM public.consultas_sql
       WHERE id=42;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C70',
              'Listado de inventario por tipo',
              sql_string,
              'SI',
              'Listado de inventario por tipo',
              ''
       FROM public.consultas_sql
       WHERE id=43;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C80',
              'Listado de travesías por tipo de suelo',
              sql_string,
              'SI',
              'Listado de travesías por tipo de suelo',
              ''
       FROM public.consultas_sql
       WHERE id=44;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C81',
              'Listado de travesías por núcleo',
              sql_string,
              'SI',
              'Listado de travesías por núcleo',
              ''
       FROM public.consultas_sql
       WHERE id=45;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C90',
              'Listado de vialidad',
              sql_string,
              'SI',
              'Listado de vialidad',
              ''
       FROM public.consultas_sql
       WHERE id=46;

DROP TABLE IF EXISTS public.consultas_sql;

INSERT INTO consultas.aforos VALUES(0, 49);
INSERT INTO consultas.aforos VALUES(50, 99);
INSERT INTO consultas.aforos VALUES(100, 249);
INSERT INTO consultas.aforos VALUES(250, 499);
INSERT INTO consultas.aforos VALUES(500, 999);
INSERT INTO consultas.aforos VALUES(1000,1999);
INSERT INTO consultas.aforos VALUES(2000,4999);
INSERT INTO consultas.aforos VALUES(5000, 9999);
INSERT INTO consultas.aforos VALUES(10000,14999);
INSERT INTO consultas.aforos VALUES(15000, 24999);
INSERT INTO consultas.aforos VALUES(25000, 100000);
