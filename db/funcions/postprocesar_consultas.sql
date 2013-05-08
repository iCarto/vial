INSERT INTO consultas.consultas
       SELECT 'C01',
              '',
              sql_string,
              'SI',
              'Inventario de carreteras',
              ''
       FROM public.consultas_sql
       WHERE id=1;

INSERT INTO consultas.consultas
       SELECT 'C10',
              'Filtros: Mayor que (ancho) / Menor que (ancho)',
              sql_string,
              'SI',
              'Ancho de firme',
              ''
       FROM public.consultas_sql
       WHERE id=2;

INSERT INTO consultas.consultas
       SELECT 'C20',
              'Filtros: Valor (tipo firme)',
              sql_string,
              'SI',
              'Tipo de firme',
              ''
       FROM public.consultas_sql
       WHERE id=3;

INSERT INTO consultas.consultas
       SELECT 'C21',
              '',
              sql_string,
              'SI',
              'Tipo de firme = MB',
              ''
       FROM public.consultas_sql
       WHERE id=4;

INSERT INTO consultas.consultas
       SELECT 'C22',
              '',
              sql_string,
              'SI',
              'Tipo de firme = TS',
              ''
       FROM public.consultas_sql
       WHERE id=5;

INSERT INTO consultas.consultas
       SELECT 'C30',
              'Filtros: Mayor que (IMD) / Menor que (IMD) / Valor (año)',
              sql_string,
              'SI',
              'Aforos',
              ''
       FROM public.consultas_sql
       WHERE id=6;

INSERT INTO consultas.consultas
       SELECT 'C40',
              'Filtros: Mayor que (cota) / Menor que (cota)',
              sql_string,
              'SI',
              'Cotas',
              ''
       FROM public.consultas_sql
       WHERE id=7;

INSERT INTO consultas.consultas
       SELECT 'C02',
              'Filtros: Valor (categoría)',
              sql_string,
              'NO',
              'Carreteras por categoría',
              ''
       FROM public.consultas_sql
       WHERE id=8;

INSERT INTO consultas.consultas
       SELECT 'C31',
              'Filtros: Mayor que (IMD) / Menor que (IMD) / Valor (año)',
              sql_string,
              'SI',
              'Aforos, donde ancho < 5',
              ''
       FROM public.consultas_sql
       WHERE id=9;

INSERT INTO consultas.consultas
       SELECT 'C32',
              'Filtros: Mayor que (IMD) / Menor que (IMD) / Valor (año)',
              sql_string,
              'SI',
              'Aforos, donde ancho >= 5 y >7',
              ''
       FROM public.consultas_sql
       WHERE id=10;

INSERT INTO consultas.consultas
       SELECT 'C33',
              'Filtros: Mayor que (IMD) / Menor que (IMD) / Valor (año)',
              sql_string,
              'SI',
              'Aforos, donde ancho > 7',
              ''
       FROM public.consultas_sql
       WHERE id=11;

INSERT INTO consultas.consultas
       SELECT 'C34',
              'Filtros: Mayor que (IMD) / Menor que (IMD) / Valor (año)',
              sql_string,
              'SI',
              'Aforos, donde tipo pavimento = TS',
              ''
       FROM public.consultas_sql
       WHERE id=12;

INSERT INTO consultas.consultas
       SELECT 'C35',
              'Filtros: Mayor que (IMD) / Menor que (IMD) / Valor (año)',
              sql_string,
              'SI',
              'Aforos, donde tipo pavimento = MB',
              ''
       FROM public.consultas_sql
       WHERE id=13;

INSERT INTO consultas.consultas
       SELECT 'C36',
              'Filtros: Mayor que (IMD) / Menor que (IMD) / Valor (año)',
              sql_string,
              'SI',
              'Aforos, donde tipo pavimento = H',
              ''
       FROM public.consultas_sql
       WHERE id=14;

INSERT INTO consultas.consultas
       SELECT 'C37',
              'Filtros: Mayor que (IMD) / Menor que (IMD) / Valor (año)',
              sql_string,
              'SI',
              'Aforos, donde tipo pavimento = O',
              ''
       FROM public.consultas_sql
       WHERE id=15;

INSERT INTO consultas.consultas
       SELECT 'C41',
              'Filtros: Mayor que (cota) / Menor que (cota)',
              sql_string,
              'SI',
              'Cotas mínimas / máximas por carretera',
              ''
       FROM public.consultas_sql
       WHERE id=16;

INSERT INTO consultas.consultas
       SELECT 'C38',
              'Filtros: Valor (año)',
              sql_string,
              'SI',
              'Aforos agregados',
              ''
       FROM public.consultas_sql
       WHERE id=17;

INSERT INTO consultas.consultas
       SELECT 'C03',
              '',
              sql_string,
              'SI',
              'Inventario de rampas',
              ''
       FROM public.consultas_sql
       WHERE id=18;

INSERT INTO consultas.consultas
       SELECT 'C04',
              '',
              sql_string,
              'SI',
              'Inventario de tramos antiguos',
              ''
       FROM public.consultas_sql
       WHERE id=19;

INSERT INTO consultas.consultas
       SELECT 'C50',
              'Filtros: Valor (año)',
              sql_string,
              'SI',
              'Listado de accidentes',
              ''
       FROM public.consultas_sql
       WHERE id=20;

INSERT INTO consultas.consultas
       SELECT 'C51',
              'Filtros: Valor (año)',
              sql_string,
              'SI',
              'Número de accidentes por tipo',
              ''
       FROM public.consultas_sql
       WHERE id=21;

INSERT INTO consultas.consultas
       SELECT 'C52',
              'Filtros: Valor (año)',
              sql_string,
              'SI',
              'Número de accidentes',
              ''
       FROM public.consultas_sql
       WHERE id=22;

-- CONSULTAS ACTUACIONES
INSERT INTO consultas.consultas_actuaciones
       SELECT 'C01',
              'Filtros: Año (fecha) / Valor (tipo)',
              sql_string,
              'SI',
              'Accidentes por tipo',
              ''
       FROM public.consultas_sql
       WHERE id=23;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C02',
              'Filtros: Año (fecha) / Valor (tipo)',
              sql_string,
              'SI',
              'Carreteras donde hubo más de 10 accidentes',
              ''
       FROM public.consultas_sql
       WHERE id=24;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C10',
              'Filtros: Año (fecha autorización) / Valor (tipo)',
              sql_string,
              'SI',
              'Autorizaciones por tipo',
              ''
       FROM public.consultas_sql
       WHERE id=25;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C11',
              'Filtros: Año (fecha autorización) / Valor (peticionario)',
              sql_string,
              'SI',
              'Autorizaciones por peticionario',
              ''
       FROM public.consultas_sql
       WHERE id=26;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C12',
              'Filtros: Año (fecha autorización) / Valor (beneficiario)',
              sql_string,
              'SI',
              'Autorizaciones por beneficiario',
              ''
       FROM public.consultas_sql
       WHERE id=27;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C13',
              'Filtros: Año (fecha autorización) / Valor (tipo)',
              sql_string,
              'SI',
              'Tasas de autorizaciones recaudadas por año y/o tipo',
              ''
       FROM public.consultas_sql
       WHERE id=28;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C20',
              'Filtros: Año (fecha aprobación presupuesto) / Valor (tipo)',
              sql_string,
              'SI',
              'Conservaciones por tipo',
              ''
       FROM public.consultas_sql
       WHERE id=29;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C21',
              'Filtros: Año (fecha aprobación presupuesto)',
              sql_string,
              'SI',
              'Importe total de conservaciones',
              ''
       FROM public.consultas_sql
       WHERE id=30;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C22',
              'Filtros: Año (fecha inicio)',
              sql_string,
              'SI',
              'Conservaciones por fecha de inicio',
              ''
       FROM public.consultas_sql
       WHERE id=31;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C23',
              'Filtros: Año (fecha fin)',
              sql_string,
              'SI',
              'Conservaciones por fecha de fin',
              ''
       FROM public.consultas_sql
       WHERE id=32;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C30',
              'Filtros: Año (fecha aprobación presupuesto) / Valor (tipo)',
              sql_string,
              'SI',
              'Propuestas de conservación por tipo',
              ''
       FROM public.consultas_sql
       WHERE id=33;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C31',
              'Filtros: Año (fecha aprobación presupuesto)',
              sql_string,
              'SI',
              'Importe total de propuestas de conservación',
              ''
       FROM public.consultas_sql
       WHERE id=34;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C40',
              'Filtros: Año (fecha aprobación presupuesto / Valor (tipo)',
              sql_string,
              'SI',
              'Construcciones por tipo',
              ''
       FROM public.consultas_sql
       WHERE id=35;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C41',
              'Filtros: Año (fecha aprobación presupuesto)',
              sql_string,
              'SI',
              'Importe total de construcciones',
              ''
       FROM public.consultas_sql
       WHERE id=36;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C50',
              'Filtros: Año (fecha denuncia) / Valor (tipo)',
              sql_string,
              'SI',
              'Denuncias por fecha de denuncia',
              ''
       FROM public.consultas_sql
       WHERE id=37;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C51',
              'Filtros: Año (fecha resolución) / Valor (tipo)',
              sql_string,
              'SI',
              'Denuncias por fecha de resolución',
              ''
       FROM public.consultas_sql
       WHERE id=38;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C52',
              'Filtros: Año (fecha denuncia) / Valor (importe sanción)',
              sql_string,
              'SI',
              'Sanciones por denuncia (importe = X)',
              ''
       FROM public.consultas_sql
       WHERE id=39;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C53',
              'Filtros: Año (fecha denuncia) / Valor (importe sanción)',
              sql_string,
              'SI',
              'Sanciones por denuncia (importe > X)',
              ''
       FROM public.consultas_sql
       WHERE id=40;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C54',
              'Filtros: Año (fecha denuncia) / Valor (importe sanción)',
              sql_string,
              'SI',
              'Sanciones por denuncia (importe < X)',
              ''
       FROM public.consultas_sql
       WHERE id=41;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C60',
              'Filtros: Año (fecha detección) / Valor (tipo)',
              sql_string,
              'SI',
              'Incidencias por fecha detección',
              ''
       FROM public.consultas_sql
       WHERE id=42;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C61',
              'Filtros: Año (fecha resolución) / Valor (tipo)',
              sql_string,
              'SI',
              'Incidencias por fecha resolución',
              ''
       FROM public.consultas_sql
       WHERE id=43;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C70',
              'Filtros: Valor (tipo)',
              sql_string,
              'SI',
              'Inventario por tipo',
              ''
       FROM public.consultas_sql
       WHERE id=44;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C80',
              'Filtros: Valor (tipo suelo)',
              sql_string,
              'SI',
              'Travesías por tipo de suelo',
              ''
       FROM public.consultas_sql
       WHERE id=45;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C81',
              'Filtros: Valor (núcleo)',
              sql_string,
              'SI',
              'Travesías por núcleo',
              ''
       FROM public.consultas_sql
       WHERE id=46;

INSERT INTO consultas.consultas_actuaciones
       SELECT 'C90',
              'Filtros: Valor (código)',
              sql_string,
              'SI',
              'Actuaciones de vialidad',
              ''
       FROM public.consultas_sql
       WHERE id=47;

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
