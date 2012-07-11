#!/bin/bash

db_config=${1}

if [ $# -ne 1 ]
then
    # use local db_config if none passed
    . db_config_local
else
    . $db_config
fi

# Functions for testing purposes
# ------------------------------
psql -h $viasobras_server -p $viasobras_port -U $viasobras_pguser \
    $viasobras_dbname < $pg_tap_install_path
