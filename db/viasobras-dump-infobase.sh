#!/bin/bash

# @author: Andrés Maneiro <andres.maneiro@gmail.com>
# @license: GPL v3

# The password should be set in .pgpass file, as the script will not issue for it
# Use -W in case you want it to ask every pg_dump execution

config_file=$1
. $config_file

pg_dump --no-owner -v -h $viasobras_server -U $viasobras_user -w \
    -n info_base -f ./datos/info_base/schema-infobase.sql $viasobras_dbname

pg_dump --no-owner -v -h $viasobras_server -U $viasobras_user -w \
     -a -t public.geometry_columns -f ./datos/info_base/table-geometrycolumns.sql $viasobras_dbname

