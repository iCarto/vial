#!/bin/bash

# @author: Andrés Maneiro <andres.maneiro@gmail.com>
# @license: GPL v3

# The password should be set in .pgpass file, as the script will not issue for it
# Use -W in case you want it to ask every pg_dump execution

config_file=$1
. $config_file

pg_dump --no-owner -v -h $viasobras_server -U $viasobras_user -w \
    -n elle -f ./db/schema-elle.sql $viasobras_dbname

