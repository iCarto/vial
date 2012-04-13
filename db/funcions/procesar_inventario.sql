-- ANCHO PLATAFORMA
DROP TABLE IF EXISTS inventario.ancho_plataforma;
CREATE TABLE inventario.ancho_plataforma (
       "gid" serial PRIMARY KEY,
       "municipio" varchar(17),
       "numeromuni" float8,
       "numeroinve" varchar(4),
       "tramo" varchar(1),
       "ancho_plataforma" float8,
       "origentram" float8,
       "finaltramo" float8
);

INSERT INTO inventario.ancho_plataforma (
       SELECT nextval('inventario.ancho_plataforma_gid_seq'),
              municipio, numeromuni, numeroinve, tramo,
              anchoplata, origentram, finaltramo
       FROM inventario.inventario
       WHERE anchoplata IS NOT NULL
);

INSERT INTO inventario.ancho_plataforma (
       SELECT nextval('inventario.ancho_plataforma_gid_seq'),
              municipio, numeromuni, numeroinve, tramo,
              to_number(anchoplat2, '999'),
              to_number(origentra2, '999D999'),
              to_number(finaltram2, '999D999')
       FROM inventario.inventario
       WHERE anchoplat2 IS NOT NULL
);

INSERT INTO inventario.ancho_plataforma (
       SELECT nextval('inventario.ancho_plataforma_gid_seq'),
              municipio, numeromuni, numeroinve, tramo,
              to_number(anchoplat3, '999'),
              to_number(origentra3, '999D999'),
              to_number(finaltram3, '999D999')
       FROM inventario.inventario
       WHERE anchoplat3 IS NOT NULL
);

-- TIPO PAVIMENTO
DROP TABLE IF EXISTS inventario.tipo_pavimento;
CREATE TABLE inventario.tipo_pavimento (
       "gid" serial PRIMARY KEY,
       "municipio" varchar(17),
       "numeromuni" float8,
       "numeroinve" varchar(4),
       "tramo" varchar(1),
       "tipopavime" varchar(4),
       "origenpavi" float8,
       "finalpavim" float8
);

INSERT INTO inventario.tipo_pavimento(
       SELECT nextval('inventario.tipo_pavimento_gid_seq'),
              municipio, numeromuni, numeroinve, tramo,
              tipopavime, origenpavi, finalpavim
       FROM inventario.inventario
       WHERE tipopavime IS NOT NULL
);

INSERT INTO inventario.tipo_pavimento(
       SELECT nextval('inventario.tipo_pavimento_gid_seq'),
              municipio, numeromuni, numeroinve, tramo,
              tipopavim2, origenpav2, finalpavi2
       FROM inventario.inventario
       WHERE tipopavim2 IS NOT NULL
);

INSERT INTO inventario.tipo_pavimento(
       SELECT nextval('inventario.tipo_pavimento_gid_seq'),
              municipio, numeromuni, numeroinve, tramo,
              tipopavim3,
              to_number(origenpav3, '999D999'),
              to_number(finalpavi3, '999D999')
       FROM inventario.inventario
       WHERE tipopavim3 IS NOT NULL
);
