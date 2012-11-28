-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.create_pks_1000() RETURNS void AS $BODY$
BEGIN

        INSERT INTO inventario.pks_1000(
               SELECT nextval('inventario.pks_1000_gid_seq'),
                      codigo_carretera,
                      generate_series(CAST(round(min(pk_inicial_tramo)) AS int) + 1,
                                      CAST(round(max(pk_final_tramo)) AS int),
                                      1) AS pk
               FROM inventario.carretera_municipio
               GROUP BY codigo_carretera
               ORDER BY codigo_carretera
        );

END;
$BODY$ LANGUAGE plpgsql;
