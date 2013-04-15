-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.create_pks_1000() RETURNS void AS $BODY$
BEGIN

        INSERT INTO inventario.pks_1000(
               SELECT nextval('inventario.pks_1000_gid_seq'),
                      codigo_carretera,
                      '',
                      generate_series(CAST(round(min(pk_inicial_tramo)) AS int) + 1,
                                      CAST(round(max(pk_final_tramo)) AS int),
                                      1) AS pk
               FROM inventario.carretera_municipio
               GROUP BY codigo_carretera
               ORDER BY codigo_carretera
        );

        UPDATE inventario.pks_1000 AS b
        SET codigo_municipio = a.codigo_municipio
        FROM inventario.carretera_municipio a
        WHERE b.codigo_carretera = a.codigo_carretera
              AND b.pk > a.pk_inicial_tramo
              AND b.pk < a.pk_final_tramo;

END;
$BODY$ LANGUAGE plpgsql;
