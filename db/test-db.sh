#!/bin/bash

db_config=${1}

if [ $# -ne 1 ]
then
    # use local db_config if none passed
    . db_config_local
else
    . $db_config
fi

pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/triggers-launch.sql
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/check-pks-ancho-plataforma.sql
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/check-pks-tipo-pavimento.sql
