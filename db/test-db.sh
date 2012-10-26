#!/bin/bash

db_config=${1}

if [ $# -ne 1 ]
then
    # use local db_config if none passed
    . db_config_local
else
    . $db_config
fi

# geometry types for layers with M-coordinate
echo -e "\n"
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/check-geometry-types.sql

# triggers
echo -e "\n"
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/triggers-launch-carretera.sql
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/triggers-launch-caracteristicas.sql

#Variantes & rampas: estado
echo -e "\n"
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/check-estado-variantes-rampas.sql

# orden tramo
echo -e "\n"
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/check-orden-tramo-ancho-plataforma.sql
echo -e "\n"
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/check-orden-tramo-cotas.sql
echo -e "\n"
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/check-orden-tramo-tipo-pavimento.sql
echo -e "\n"
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/check-orden-tramo-aforos.sql
echo -e "\n"
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/check-orden-tramo-accidentes.sql

# PKs
echo -e "\n"
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/check-pks-actuaciones.sql
echo -e "\n"
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/check-pks-ancho-plataforma.sql
echo -e "\n"
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/check-pks-cotas.sql
echo -e "\n"
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/check-pks-tipo-pavimento.sql
echo -e "\n"
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/check-pks-aforos.sql
echo -e "\n"
pg_prove -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    -d $viasobras_dbname ./tests/check-pks-accidentes.sql
