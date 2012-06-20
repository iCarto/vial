DROP TRIGGER IF EXISTS update_geom_tipo_pavimento ON inventario.tipo_pavimento;
CREATE TRIGGER update_geom_tipo_pavimento
       BEFORE UPDATE OR INSERT
       ON inventario.tipo_pavimento FOR EACH ROW
       EXECUTE PROCEDURE inventario.trigger_update_geom_tipo_pavimento();

DROP TRIGGER IF EXISTS update_geom_ancho_plataforma ON inventario.ancho_plataforma;
CREATE TRIGGER update_geom_ancho_plataforma
       BEFORE UPDATE OR INSERT
       ON inventario.ancho_plataforma FOR EACH ROW
       EXECUTE PROCEDURE inventario.trigger_update_geom_ancho_plataforma();
