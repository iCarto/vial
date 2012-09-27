BEGIN;

SELECT plan(16);

-- Test triggers are in place
SELECT trigger_is('inventario',
                  'tipo_pavimento',
                  'update_geom_tipo_pavimento',
                  'inventario',
                  'update_geom_line_on_pk_change');

SELECT trigger_is('inventario',
                  'tipo_pavimento',
                  'update_longitud',
                  'inventario',
                  'update_longitud');

SELECT trigger_is('inventario',
                  'ancho_plataforma',
                  'update_geom_ancho_plataforma',
                  'inventario',
                  'update_geom_line_on_pk_change');

SELECT trigger_is('inventario',
                  'ancho_plataforma',
                  'update_longitud',
                  'inventario',
                  'update_longitud');

SELECT trigger_is('inventario',
                  'cotas',
                  'update_geom_cotas',
                  'inventario',
                  'update_geom_line_on_pk_change');

SELECT trigger_is('inventario',
                  'cotas',
                  'update_longitud',
                  'inventario',
                  'update_longitud');

SELECT trigger_is('inventario',
                  'actuaciones',
                  'update_geom_actuaciones',
                  'inventario',
                  'update_geom_line_on_pk_change');

SELECT trigger_is('inventario',
                  'aforos',
                  'update_geom_aforos',
                  'inventario',
                  'update_geom_point_on_pk_change');

-- Test they work

INSERT INTO inventario.tipo_pavimento
       (codigo_carretera, codigo_municipio, valor, pk_inicial, pk_final)
       VALUES('0101', '27018', 'test', 10.2, 10.4);
SELECT isnt(the_geom, NULL, 'tipo_pavimento - geom calculated on INSERT')
       FROM inventario.tipo_pavimento
       WHERE gid = currval('inventario.tipo_pavimento_gid_seq');

INSERT INTO inventario.tipo_pavimento
       (codigo_carretera, codigo_municipio, valor, pk_inicial, pk_final)
       VALUES('0101', '27018', 'test', 8, 12);
SELECT is(longitud, '4000', 'tipo_pavimento - longitud calculated on INSERT')
       FROM inventario.tipo_pavimento
       WHERE gid = currval('inventario.tipo_pavimento_gid_seq');

INSERT INTO inventario.ancho_plataforma
       (codigo_carretera, codigo_municipio, valor, pk_inicial, pk_final)
       VALUES('0101', '27018', 666.666, 10.2, 10.4);
SELECT isnt(the_geom, NULL, 'ancho_plataforma - geom calculated on INSERT')
       FROM inventario.ancho_plataforma
       WHERE gid = currval('inventario.ancho_plataforma_gid_seq');

INSERT INTO inventario.ancho_plataforma
       (codigo_carretera, codigo_municipio, valor, pk_inicial, pk_final)
       VALUES('0101', '27018', 666.666, 9, 15);
SELECT is(longitud, '6000', 'ancho_plataforma - longitud calculated on INSERT')
       FROM inventario.ancho_plataforma
       WHERE gid = currval('inventario.ancho_plataforma_gid_seq');

INSERT INTO inventario.cotas
       (codigo_carretera, codigo_municipio, valor, pk_inicial, pk_final)
       VALUES('0101', '27018', 450, 10.2, 10.4);
SELECT isnt(the_geom, NULL, 'cotas - geom calculated on INSERT')
       FROM inventario.cotas
       WHERE gid = currval('inventario.cotas_gid_seq');

INSERT INTO inventario.cotas
       (codigo_carretera, codigo_municipio, valor, pk_inicial, pk_final)
       VALUES('0101', '27018', 450, 2, 7.5);
SELECT is(longitud, '5500', 'cotas - geom calculated on INSERT')
       FROM inventario.cotas
       WHERE gid = currval('inventario.cotas_gid_seq');

INSERT INTO inventario.actuaciones
       (codigo_actuacion, codigo_carretera, pk_inicial, pk_final)
       VALUES ('1234', '0101', 0, 2);
SELECT isnt(the_geom, NULL, 'actuaciones - geom calculated on INSERT')
       FROM inventario.cotas
       WHERE gid = currval('inventario.actuaciones_gid_seq');

INSERT INTO inventario.aforos
       (codigo_carretera, codigo_municipio, valor, pk)
       VALUES ('0101', '27018', 12, 2);
SELECT isnt(the_geom, NULL, 'aforos - geom calculated on INSERT')
       FROM inventario.aforos
       WHERE gid = currval('inventario.aforos_gid_seq');

ROLLBACK;
