
SELECT AddGeometryColumn('inventario', 'ancho_plataforma', 'the_geom', '23029', 'MULTILINESTRINGM', 3);
ALTER TABLE inventario.ancho_plataforma DROP CONSTRAINT enforce_geotype_the_geom;
SELECT inventario.update_geom_ancho_plataforma_all();

SELECT AddGeometryColumn('inventario', 'tipo_pavimento', 'the_geom', '23029', 'MULTILINESTRINGM', 3);
ALTER TABLE inventario.tipo_pavimento DROP CONSTRAINT enforce_geotype_the_geom;
SELECT inventario.update_geom_tipo_pavimento_all();

