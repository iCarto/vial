-- ANCHO PLATAFORMA
DROP TABLE IF EXISTS inventario.ancho_plataforma;
CREATE TABLE inventario.ancho_plataforma (
       "gid" serial PRIMARY KEY,
       "carretera" varchar(4),
       "nombre" varchar(21),
       "municipio" varchar(5),
       "tramo" varchar(1),
       "ancho_plataforma" float8,
       "origentram" float8,
       "finaltramo" float8
);

INSERT INTO inventario.ancho_plataforma (
       SELECT nextval('inventario.ancho_plataforma_gid_seq'),
              numeroinve, municipio, '', tramo,
              anchoplata, origentram, finaltramo
       FROM inventario.inventario
       WHERE anchoplata IS NOT NULL
);
UPDATE inventario.ancho_plataforma SET municipio = c.codigo
       FROM inventario.municipio_codigo AS c
       WHERE inventario.ancho_plataforma.nombre = c.nombre;

-- INSERT INTO inventario.ancho_plataforma (
--        SELECT nextval('inventario.ancho_plataforma_gid_seq'),
--               municipio, numeromuni, numeroinve, tramo,
--               to_number(anchoplat2, '999'),
--               to_number(origentra2, '999D999'),
--               to_number(finaltram2, '999D999')
--        FROM inventario.inventario
--        WHERE anchoplat2 IS NOT NULL
-- );

-- INSERT INTO inventario.ancho_plataforma (
--        SELECT nextval('inventario.ancho_plataforma_gid_seq'),
--               municipio, numeromuni, numeroinve, tramo,
--               to_number(anchoplat3, '999'),
--               to_number(origentra3, '999D999'),
--               to_number(finaltram3, '999D999')
--        FROM inventario.inventario
--        WHERE anchoplat3 IS NOT NULL
-- );

-- TIPO PAVIMENTO
DROP TABLE IF EXISTS inventario.tipo_pavimento;
CREATE TABLE inventario.tipo_pavimento (
       "gid" serial PRIMARY KEY,
       "carretera" varchar(4),
       "nombre" varchar(21),
       "municipio" varchar(5),
       "tramo" varchar(1),
       "tipopavime" varchar(5),
       "origenpavi" float8,
       "finalpavim" float8
);

INSERT INTO inventario.tipo_pavimento(
       SELECT nextval('inventario.tipo_pavimento_gid_seq'),
              numeroinve, municipio, '', tramo,
              tipopavime,
              to_number(origenpavi, '999D999'),
              to_number(finalpavim, '999D999')
       FROM inventario.inventario
       WHERE tipopavime IS NOT NULL
);
UPDATE inventario.tipo_pavimento SET municipio = c.codigo
       FROM inventario.municipio_codigo AS c
       WHERE inventario.tipo_pavimento.nombre = c.nombre;

-- INSERT INTO inventario.tipo_pavimento(
--        SELECT nextval('inventario.tipo_pavimento_gid_seq'),
--               municipio, numeromuni, numeroinve, tramo,
--               tipopavim2, origenpav2, finalpavi2
--        FROM inventario.inventario
--        WHERE tipopavim2 IS NOT NULL
-- );

-- INSERT INTO inventario.tipo_pavimento(
--        SELECT nextval('inventario.tipo_pavimento_gid_seq'),
--               municipio, numeromuni, numeroinve, tramo,
--               tipopavim3,
--               to_number(origenpav3, '999D999'),
--               to_number(finalpavi3, '999D999')
--        FROM inventario.inventario
--        WHERE tipopavim3 IS NOT NULL
-- );

DROP TABLE IF EXISTS inventario.inventario;
