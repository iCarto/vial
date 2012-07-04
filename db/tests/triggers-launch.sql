BEGIN;

SELECT plan(4);

-- Test triggers are in place
SELECT trigger_is('inventario',
                  'tipo_pavimento',
                  'update_geom_tipo_pavimento',
                  'inventario',
                  'update_geom_tipo_pavimento');

SELECT trigger_is('inventario',
                  'ancho_plataforma',
                  'update_geom_ancho_plataforma',
                  'inventario',
                  'update_geom_ancho_plataforma');

-- Test they work

INSERT INTO inventario.tipo_pavimento
       (carretera, municipio, tipopavime, origenpavi, finalpavim)
       VALUES('0101', '27018', 'test', 10.2, 10.4);
SELECT isnt(the_geom, NULL, 'tipo_pavimento - geom calculated on INSERT')
       FROM inventario.tipo_pavimento
       WHERE gid = currval('inventario.tipo_pavimento_gid_seq');

INSERT INTO inventario.ancho_plataforma
       (carretera, municipio, ancho_plataforma, origentram, finaltramo)
       VALUES('0101', '27018', 666.666, 10.2, 10.4);
SELECT isnt(the_geom, NULL, 'ancho_plataforma - geom calculated on INSERT')
       FROM inventario.ancho_plataforma
       WHERE gid = currval('inventario.ancho_plataforma_gid_seq');

ROLLBACK;
