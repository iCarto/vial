-- ESQUEMA FUNCIONS
DROP SCHEMA IF EXISTS aplicacions;
CREATE SCHEMA aplicacions;

CREATE OR REPLACE FUNCTION aplicacions.insert_data_in_carreteras(
       tabla_origen character varying,
       tabla_destino character varying)

$BODY$ BEGIN

EXECUTE('INSERT INTO '||tabla_destino||' (
                x, y
)
                SELECT x, y
                FROM '||tabla_origen||';
');

END $BODY

LANGUAGE plpgsql VOLATILE COST 100;
