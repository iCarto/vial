#!/bin/bash

config_file=$1
. $config_file

# Queries
# -------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create_schema_consultas.sql
