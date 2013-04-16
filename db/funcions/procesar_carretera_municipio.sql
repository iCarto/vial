DROP TRIGGER IF EXISTS update_longitud_carretera_municipio ON inventario.carretera_municipio;
CREATE TRIGGER update_longitud_carretera_municipio
       BEFORE UPDATE OR INSERT
       ON inventario.carretera_municipio FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_longitud_carretera_municipio();

DROP TRIGGER IF EXISTS update_pks_carreteras ON inventario.carretera_municipio;
CREATE TRIGGER update_pks_carreteras
       AFTER UPDATE OR INSERT OR DELETE
       ON inventario.carretera_municipio FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_pks_carreteras();

-- this trigger will be executed after update_pks_carreteras,
-- as both are executed for AFTER UPDATE or INSERT events,
-- alphabetical ordering sets the order of execution
DROP TRIGGER IF EXISTS update_pks_m_1000 ON inventario.carretera_municipio;
CREATE TRIGGER update_pks_m_1000
       AFTER UPDATE OR INSERT OR DELETE
       ON inventario.carretera_municipio FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_pks_1000();
