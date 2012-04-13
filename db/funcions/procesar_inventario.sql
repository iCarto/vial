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
       SELECT NULL,
              municipio, numeromuni, numeroinve, tramo,
              to_number(anchoplat3, '999'),
              to_number(origentra3, '999D999'),
              to_number(finaltram3, '999D999')
       FROM inventario.inventario
       WHERE anchoplat3 IS NOT NULL
);
