BEGIN;

--definition
DROP TABLE IF EXISTS inventario.accidentes;
CREATE TABLE inventario.accidentes (
       gid SERIAL,
       codigo_carretera text,
       codigo_municipio text,
       tramo text,
       pk float,
       fecha date,
       valor text,
       poblacion text,
       sentido text,
       luminosidad text,
       superficie text,
       visibilidad_restringida_por text,
       factores_atmosfericos text,
       mediana text,
       barrera_seguridad text,
       paneles_direccionales text,
       hitos_arista text,
       capta_faros text,
       prioridad_regulada_por text,
       circulacion text,
       circulacion_medidas_especiales text,
       interseccion_con text,
       tipo_interseccion text,
       acondicionamiento_interseccion text,
       fuera_interseccion text,
       tipo_accidente text,
       muertos integer,
       heridos_graves integer,
       heridos_leves integer,
       vehiculos_implicados integer,
       id_accidente text,
       PRIMARY KEY(gid),
       FOREIGN KEY (codigo_carretera) REFERENCES inventario.carreteras (numero)
               ON DELETE CASCADE
               ON UPDATE CASCADE
);

-- populate it
INSERT INTO inventario.accidentes (
       SELECT nextval('inventario.accidentes_gid_seq') AS gid,
              "numero" AS codigo_carretera,
              to_char("cod_mun_lu", 'FM99999') AS codigo_municipio,
              "tramo" AS tramo,
              "pk" AS pk,
              "fecha" AS fecha,
              "tipo_accid" AS valor,
              "poblacion" AS poblacion,
              "se" AS sentido,
              "luminosida" AS luminosidad,
              "superficie" AS superficie,
              "visibilida" AS visibilidad_restringida_por,
              "factores_a" AS factores_atmosfericos,
              "mediana_en" AS mediana,
              "barrera_se" AS barrera_seguridad,
              "paneles_di" AS paneles_direccionales,
              "hitos_aris" AS hitos_arista,
              "capta_faro" AS capta_faros,
              "prioridad_" AS prioridad_regulada_por,
              "circulacio" AS circulacion,
              "circulaci2" AS circulacion_medidas_especiales,
              "intersecci" AS interseccion_con,
              "tipo_otros" AS tipo_interseccion,
              "acondicion" AS acondicionamiento_interseccion,
              "fuera_inte" AS fuera_interseccion,
              "tipo_accid" AS tipo_accidente,
              "m" AS muertos,
              "hg" AS heridos_graves,
              "hl" AS heridos_leves,
              "t_vehi" AS vehiculos_implicados,
              "id_acciden" AS id_accidente
       FROM inventario.accidentes_tmp
);

-- linear referencing
SELECT AddGeometryColumn('inventario', 'accidentes', 'the_geom', 25829, 'POINTM', 3);
ALTER TABLE inventario.accidentes DROP CONSTRAINT enforce_geotype_the_geom;
ALTER TABLE inventario.accidentes DROP CONSTRAINT enforce_dims_the_geom;
SELECT inventario.update_geom_point_all('inventario', 'accidentes');

-- indexes
CREATE INDEX accidentes_the_geom
       ON inventario.accidentes USING GIST(the_geom);
CREATE INDEX accidentes_codigo_carretera
       ON inventario.accidentes USING BTREE(codigo_carretera);
CREATE INDEX accidentes_codigo_municipio
       ON inventario.accidentes USING BTREE(codigo_municipio);
CREATE INDEX accidentes_codigo_carretera_concello
       ON inventario.accidentes USING BTREE(codigo_carretera, codigo_municipio);

-- triggers
DROP TRIGGER IF EXISTS update_geom_accidentes ON inventario.accidentes;
CREATE TRIGGER update_geom_accidentes
       BEFORE UPDATE OR INSERT
       ON inventario.accidentes FOR EACH ROW
       EXECUTE PROCEDURE inventario.update_geom_point_on_pk_change();

DROP TABLE IF EXISTS inventario.accidentes_tmp;

COMMIT;
