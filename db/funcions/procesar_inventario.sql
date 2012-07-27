-- ANCHO PLATAFORMA
DROP TABLE IF EXISTS inventario.ancho_plataforma;
CREATE TABLE inventario.ancho_plataforma (
       gid serial PRIMARY KEY,
       codigo_carretera varchar(4),
       codigo_concello varchar(5),
       nome_concello varchar(21),
       pk_inicial float8,
       pk_final float8,
       valor double precision
);

INSERT INTO inventario.ancho_plataforma (
       SELECT nextval('inventario.ancho_plataforma_gid_seq'),
              numeroinve, '', municipio, origentram, finaltramo,
              anchoplata
       FROM inventario.inventario
       WHERE anchoplata IS NOT NULL
);
UPDATE inventario.ancho_plataforma SET codigo_concello = c.codigo
       FROM inventario.municipio_codigo AS c
       WHERE inventario.ancho_plataforma.nome_concello = c.nombre;
ALTER TABLE inventario.ancho_plataforma
      DROP COLUMN nome_concello;
DELETE FROM inventario.ancho_plataforma WHERE
       pk_inicial IS NULL AND pk_final IS NULL;

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
       gid serial PRIMARY KEY,
       codigo_carretera varchar(4),
       codigo_concello varchar(5),
       nome_concello varchar(21),
       pk_inicial float8,
       pk_final float8,
       valor varchar(5)
);

INSERT INTO inventario.tipo_pavimento(
       SELECT nextval('inventario.tipo_pavimento_gid_seq'),
              numeroinve, '', municipio,
              to_number(origenpavi, '999D999'),
              to_number(finalpavim, '999D999'),
              tipopavime
       FROM inventario.inventario
       WHERE tipopavime IS NOT NULL
);
UPDATE inventario.tipo_pavimento SET codigo_concello = c.codigo
       FROM inventario.municipio_codigo AS c
       WHERE inventario.tipo_pavimento.nome_concello = c.nombre;
ALTER TABLE inventario.tipo_pavimento
      DROP COLUMN nome_concello;
DELETE FROM inventario.tipo_pavimento WHERE
       pk_inicial IS NULL OR pk_final IS NULL;

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
