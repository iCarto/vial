#!/bin/bash

# Require having a template with PostGIS support (ie: template_postgis)

config_file=$1
. $config_file


psql -h $viasobras_server -p $viasobras_port -U $viasobras_superuser \
    $viasobras_dbname -c "VACUUM ANALYZE;"
