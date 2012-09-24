-- @author: Andrés Maneiro <andres.maneiro@gmail.com>
-- @license: GPL v3

CREATE OR REPLACE FUNCTION inventario.update_tramos_in_range(
       the_schema_name TEXT,
       the_table_name TEXT,
       the_carretera_code varchar(4),
       pk_inicial_range double precision,
       pk_final_range double precision,
       the_offset double precision) RETURNS void AS $BODY$
BEGIN

-- Actions on tramos:
-- * under the range (pk_final < pk_inicial_range): do nothing
-- * touching pk_inicial_range: lower the pk_final
-- * completely within the range (pk_inicial >= pk_inicial_range & pk_final <= pk_final_range): delete them
-- * touching pk_final_range: upper the pk_inicial
-- * over the range (pk_inicial > pk_final_range): update its pk_inicial & pk_final by summing the offset

-- Tramos touching range pk
EXECUTE 'INSERT INTO inventario.tipo_pavimento (
            codigo_carretera, codigo_municipio, tramo, pk_inicial, pk_final,
            longitud, fecha_actualizacion, valor, observaciones)
         SELECT codigo_carretera, codigo_municipio, tramo, '||pk_inicial_range||' AS pk_inicial, pk_final,
                longitud, fecha_actualizacion, valor, observaciones
         FROM inventario.tipo_pavimento
         WHERE codigo_carretera = '''||the_carretera_code||'''
               AND pk_inicial < '||pk_inicial_range||'
               AND pk_final > '||pk_inicial_range||';';
EXECUTE 'UPDATE '||the_schema_name||'.'||the_table_name||'
       SET pk_final = '||pk_inicial_range||'
       WHERE codigo_carretera = '''||the_carretera_code||'''
             AND pk_inicial < '||pk_inicial_range||'
             AND pk_final > '||pk_inicial_range||';';
EXECUTE 'UPDATE '||the_schema_name||'.'||the_table_name||'
       SET pk_inicial = '||pk_final_range||'
       WHERE codigo_carretera = '''||the_carretera_code||'''
             AND pk_inicial < '||pk_final_range||'
             AND pk_final > '||pk_final_range||';';

-- Tramos within the range
EXECUTE 'DELETE FROM '||the_schema_name||'.'||the_table_name||'
       WHERE codigo_carretera = '''||the_carretera_code||'''
             AND pk_inicial >='||pk_inicial_range||'
             AND pk_final <='||pk_final_range||';';

-- Tramos over the range
EXECUTE 'UPDATE '||the_schema_name||'.'||the_table_name||'
       SET pk_inicial = pk_inicial + '||the_offset||',
           pk_final = pk_final + '||the_offset||'
       WHERE codigo_carretera = '''||the_carretera_code||'''
             AND pk_inicial >= '||pk_final_range||';';

END;
$BODY$ LANGUAGE plpgsql;
