#!/bin/bash

config_file=$1
. $config_file

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create_schema_inventario.sql

# Import useful functions
#-------------------------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_geom_line_all.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_geom_point_all.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_geom_line_on_pk_change.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_geom_point_on_pk_change.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_geom_line_on_pk_carretera_change.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/calibrate_carretera_all.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/calibrate_carretera.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/recalculate_caracteristicas.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/readjust_tramos.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_tramos_in_range.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/mirror_carreteras_lugo.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_longitud.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_longitud_carretera_municipio.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create_pks_1000.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_pks_1000.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_pks_carreteras.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/update_codigo_carretera.sql

# Red carreteras
#---------------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/inventario/carreteras_tmp.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/procesar_carreteras_lugo.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/procesar_carreteras.sql

# Rampas & variantes
#-------------------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/inventario/variantes_rampas_tmp.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/procesar_variantes.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/procesar_rampas.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/procesar_variantes_rampas.sql

# Accidentes
#-----------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/inventario/accidentes_tmp.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/procesar_accidentes.sql

# Aforos
#-------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < datos/inventario/aforos_tmp.sql
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
   $viasobras_dbname < funcions/procesar_aforos.sql

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
sql_query="\COPY inventario.carretera_municipio (codigo_carretera, orden_tramo, codigo_municipio, pk_inicial_tramo, pk_final_tramo, longitud_tramo, observaciones_tramo) FROM '$csv_path' WITH DELIMITER ';'"
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname -c "$sql_query"

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/procesar_carretera_municipio.sql

# PKS
#-----
psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/procesar_pks_from_carreteras.sql

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

# Actuaciones
#------------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/procesar_actuaciones.sql

# Actuaciones - concellos
#------------------------

psql -h $viasobras_server -p $viasobras_port -U $viasobras_user \
    $viasobras_dbname < funcions/create-table-inventario-actuacionmunicipio.sql
