BEGIN;

DROP TABLE IF EXISTS inventario.tipo_pavimento;
CREATE TABLE inventario.tipo_pavimento (
       gid serial,
       codigo_carretera varchar(4),
       codigo_municipio varchar(5),
       tramo varchar(1),
       pk_inicial float,
       pk_final float,
       longitud float,
       fecha_actualizacion date,
       valor varchar(6),
       observaciones text,
       PRIMARY KEY(gid)
);

INSERT INTO inventario.tipo_pavimento(
       SELECT nextval('inventario.tipo_pavimento_gid_seq') AS gid,
              "numero_inv" AS codigo_carretera,
              to_char("cod_munici", 'FM99999') AS codigo_municipio,
              "tramo" AS tramo,
              "pk_ini" AS pk_inicial,
              "pk_fin" AS pk_final,
              "longitud_t" AS longitud,
              NULL AS fecha_actualizacion,
              "tipo_de_pa" AS valor,
              "observacio" AS observaciones
       FROM inventario.tipo_pavimento_tmp
);

DELETE FROM inventario.tipo_pavimento WHERE
       pk_inicial IS NULL OR pk_final IS NULL;

DROP TABLE IF EXISTS inventario.tipo_pavimento_tmp;

COMMIT;
