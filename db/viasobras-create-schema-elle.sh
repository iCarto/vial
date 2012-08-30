#!/bin/bash

config_file=$1
. $config_file

# ELLE: create default map with styles
# ------------------------------------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/drop_schema_elle.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/elle/schema-elle.sql

