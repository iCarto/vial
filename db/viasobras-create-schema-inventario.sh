#!/bin/bash

config_file=$1
. $config_file

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create_schema_inventario.sql

# Red carreteras
#---------------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/inventario/red_carreteras_tmp.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/procesar_red_carreteras.sql

# Import useful functions
#-------------------------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_geom_all.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_geom_on_pk_change.sql

# Accidentes
#-----------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/inventario/accidentes.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create_accidentes_event_points.sql

# Aforos
#-------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/inventario/aforos.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
   $viasobras_dbname < funcions/procesar_aforos.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create_aforos_event_points.sql

# Municipio - codigo
#-------------------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create-table-inventario-municipiocodigo.sql

# Populate it from CSV
csv_path=`pwd`/datos/inventario/municipio_codigo.csv #COPY command needs absolute path
sql_query="\COPY inventario.municipio_codigo (nombre, codigo) FROM '$csv_path' WITH DELIMITER ','"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

# Carreteras - municipio
#-----------------------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create-table-inventario-carretera-municipio.sql

# Populate it from CSV
csv_path=`pwd`/datos/inventario/carretera_municipio.csv #COPY command needs absolute path
sql_query="\COPY inventario.carretera_municipio (codigo_carretera, codigo_municipio) FROM '$csv_path' WITH DELIMITER ','"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

# Ancho plataforma
#-----------------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/inventario/ancho_plataforma_tmp.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/procesar_ancho_plataforma.sql

# Tipo pavimento
#---------------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/inventario/tipo_pavimento_tmp.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/procesar_tipo_pavimento.sql

# Cotas
#------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/inventario/cotas_tmp.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/procesar_cotas.sql

# Actuacions - concellos
#------------------------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create-table-inventario-actuacionsconcellos.sql

# TODO: this is fake data, replace it for proper inputs
csv_path=`pwd`/datos/inventario/actuacions_concellos.csv #COPY command needs absolute path
sql_query="\COPY inventario.actuacions_concellos (codigo_actuacion, codigo_concello) FROM '$csv_path' WITH DELIMITER ','"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

# Actuacions
#-----------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/procesar_actuacions.sql