#!/bin/bash

# Require having a template with PostGIS support (ie: template_postgis)

config_file=$1
. $config_file

# Setting permisions to read only user
SCHEMAS=(consultas elle info_base inventario public)
for i in "${SCHEMAS[@]}"; do
    psql -h $viasobras_server -U $viasobras_superuser -p $viasobras_port -c "grant usage on schema $i to $viasobras_user_lectura" $viasobras_dbname;
    psql -h $viasobras_server -U $viasobras_superuser -p $viasobras_port -c "grant select on all tables in schema $i to $viasobras_user_lectura" $viasobras_dbname;
    psql -h $viasobras_server -U $viasobras_superuser -p $viasobras_port -c "alter default privileges in schema $i grant select on tables to $viasobras_user_lectura" $viasobras_dbname;
done

psql -h $viasobras_server -p $viasobras_port -U $viasobras_superuser \
    $viasobras_dbname -c "VACUUM ANALYZE;"
