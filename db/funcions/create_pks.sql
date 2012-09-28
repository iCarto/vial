-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.create_pks() RETURNS void AS $BODY$
BEGIN

        INSERT INTO public.pks(
               SELECT nextval('public.pks_gid_seq'),
                      codigo_carretera,
                      codigo_municipio,
                      generate_series(CAST(round(min(pk_inicial_tramo)) AS int) + 1,
                                      CAST(round(max(pk_final_tramo)) AS int),
                                      1) AS pk
               FROM inventario.carretera_municipio
               GROUP BY codigo_carretera, codigo_municipio
               ORDER BY codigo_carretera
        );

END;
$BODY$ LANGUAGE plpgsql;
