#!/bin/bash

db_config=${1}

if [ $# -ne 1 ]
then
    # use local db_config if none passed
    . db_config_local
else
    . $db_config
fi

# Create user and database
# -------------------------

dropdb -h $viasobras_server -p $viasobras_port -U $viasobras_superuser $viasobras_dbname;
dropuser -h $viasobras_server -p $viasobras_port -U $viasobras_superuser $viasobras_user
createuser -h $viasobras_server -p $viasobras_port -U $viasobras_superuser -SDRPl $viasobras_user
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

# ELLE: create default map with styles
# ------------------------------------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/elle.sql

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

# Queries
# -------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create_schema_queries.sql

# Inventario
# ----------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create_schema_inventario.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/inventario/rede_carreteras.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/inventario/carreteras.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/inventario/accidentes.sql

# Import functions
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_geom_tipo_pavimento_all.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_geom_ancho_plataforma_all.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_geom_tipo_pavimento.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_geom_ancho_plataforma.sql

# Post-procesado to create final tables from aforos.sql & inventario.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/inventario/aforos.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
   $viasobras_dbname < funcions/procesar_aforos.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/inventario/inventario.sql

# Import from CSV aux data to process inventario
csv_path=`pwd`/datos/inventario/municipio_codigo.csv #COPY command needs absolute path
sql_query="\COPY inventario.municipio_codigo (nombre, codigo) FROM '$csv_path' WITH DELIMITER ','"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
   $viasobras_dbname < funcions/procesar_inventario.sql

# Import from CSV link data between carreteras-concellos
csv_path=`pwd`/datos/inventario/carreteras_concellos.csv #COPY command needs absolute path
sql_query="\COPY inventario.carreteras_concellos (codigo_carretera, codigo_concello) FROM '$csv_path' WITH DELIMITER ','"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

# Linear referencing: calibrate road, event points & dynamic segmentation
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/calibrate_carreteras.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create_accidentes_event_points.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create_aforos_event_points.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create_dynamic_segments_from_inventario.sql

#Create triggers
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create_triggers.sql
