BEGIN;

SELECT plan(COUNT(*)::int)
       FROM elle._map
       WHERE nombre_tabla='pks_1000';

-- PKs layers needs to be named PKs,
-- as FormCarreterasMunicipios use the name to reload
-- the layer from TOC if there is such a layer.
-- If it's not reloaded, gvSIG will show an error after
-- labeling PKs and force a recalculation
-- (ie: updating a carretera/municipio tramo)
SELECT is(nombre_capa, 'PKs', 'PK layername is PKs')
       FROM elle._map
       WHERE nombre_tabla='pks_1000';

ROLLBACK;
