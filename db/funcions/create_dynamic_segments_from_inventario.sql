
SELECT AddGeometryColumn('inventario', 'ancho_plataforma', 'the_geom', '23029', 'MULTILINESTRINGM', 3);
ALTER TABLE inventario.ancho_plataforma DROP CONSTRAINT enforce_geotype_the_geom;

UPDATE inventario.ancho_plataforma AS a2 SET the_geom = (
       SELECT ST_CollectionExtract(ST_Locate_Between_Measures(c.the_geom,
                                a.origentram,
                                a.finaltramo), 2)
       FROM inventario.rede_carreteras AS c, inventario.ancho_plataforma AS a
       WHERE c.codigo = a.numeroinve AND a.gid = a2.gid)
;

SELECT AddGeometryColumn('inventario', 'tipo_pavimento', 'the_geom', '23029', 'MULTILINESTRINGM', 3);

UPDATE inventario.tipo_pavimento AS p2 SET the_geom = (
       SELECT ST_CollectionExtract(ST_Locate_Between_Measures(c.the_geom,
                                p.origenpavi,
                                p.finalpavim), 2)
       FROM inventario.rede_carreteras AS c, inventario.tipo_pavimento AS p
       WHERE c.codigo = p.numeroinve AND p.gid = p2.gid)
;
