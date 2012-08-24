#!/bin/bash

config_file=$1
. $config_file

# Info base
# ---------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create_schema_infobase.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/info_base/oceano.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/info_base/portugal.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/info_base/provincias_galicia.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/info_base/provincias_limitrofes.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/info_base/concellos.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/info_base/nucleos.sql
