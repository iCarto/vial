BEGIN;

DROP TABLE IF EXISTS inventario.ancho_plataforma;
CREATE TABLE inventario.ancho_plataforma (
       gid serial,
       codigo_carretera varchar(4),
       codigo_municipio varchar(5),
       tramo varchar(1),
       pk_inicial float,
       pk_final float,
       longitud float,
       fecha_actualizacion date,
       valor double precision,
       observaciones text,
       PRIMARY KEY(gid)
);

INSERT INTO inventario.ancho_plataforma(
       SELECT nextval('inventario.ancho_plataforma_gid_seq') AS gid,
              "numero_inv" AS codigo_carretera,
              to_char("cod_munici", 'FM99999') AS codigo_municipio,
              "tramo" AS tramo,
              "pk_ini" AS pk_inicial,
              "pk_fin" AS pk_final,
              "longitud_t" AS longitud,
              NULL AS fecha_actualizacion,
              "ancho_medi" AS valor,
              "observacio" AS observaciones
       FROM inventario.ancho_plataforma_tmp
);

DELETE FROM inventario.ancho_plataforma WHERE
       pk_inicial IS NULL AND pk_final IS NULL;

DROP TABLE IF EXISTS inventario.ancho_plataforma_tmp;

COMMIT;
