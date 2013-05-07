#!/bin/bash

# Require having a template with PostGIS support (ie: template_postgis)

config_file=$1
. $config_file

# Create user and database
# -------------------------

dropdb -h $viasobras_server -p $viasobras_port -U $viasobras_superuser $viasobras_dbname;
dropuser -h $viasobras_server -p $viasobras_port -U $viasobras_superuser $viasobras_user
createuser -h $viasobras_server -p $viasobras_port -U $viasobras_superuser -SDRPl $viasobras_user
createuser -h $viasobras_server -p $viasobras_port -U $viasobras_superuser -SDRPl $viasobras_user_lectura
createdb -h $viasobras_server -p $viasobras_port -U $viasobras_superuser -O $viasobras_user \
    -T $viasobras_template $viasobras_dbname;

psql -h $viasobras_server -p $viasobras_port -U $viasobras_superuser \
    $viasobras_dbname -c "ALTER DATABASE $viasobras_dbname OWNER TO $viasobras_user;"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_superuser \
    $viasobras_dbname -c "ALTER SCHEMA public OWNER TO $viasobras_user;"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_superuser \
    $viasobras_dbname -c "ALTER TABLE public.geometry_columns OWNER TO $viasobras_user;"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_superuser \
    $viasobras_dbname -c "ALTER TABLE public.spatial_ref_sys OWNER TO $viasobras_user;"
