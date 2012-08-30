#!/bin/bash

config_file=$1
. $config_file

# Info base
# ---------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/drop_schema_infobase.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/info_base/schema-infobase.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/info_base/table-geometrycolumns.sql
