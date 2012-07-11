#!/bin/bash

. db_config

# info_base
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I\
    ${viasobras_data_orig}/info_base/contorno/Oceano \
    info_base.oceano > datos/info_base/oceano.sql
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I\
    ${viasobras_data_orig}/info_base/contorno/Portugal \
    info_base.portugal > datos/info_base/portugal.sql
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I\
    ${viasobras_data_orig}/info_base/contorno/Provincias_Galicia \
    info_base.provincias_galicia > datos/info_base/provincias_galicia.sql
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I\
    ${viasobras_data_orig}/info_base/contorno/Provincias_limitrofes \
    info_base.provincias_limitrofes > datos/info_base/provincias_limitrofes.sql
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I\
    ${viasobras_data_orig}/info_base/municipios/municipios_lugo_text \
    info_base.concellos > datos/info_base/concellos.sql
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I\
    ${viasobras_data_orig}/info_base/nucleos/Nuc_INE_99_09 \
    info_base.nucleos > datos/info_base/nucleos.sql

# inventario
shp2pgsql -d -W iso-8859-1 \
    ${viasobras_data_orig}/inventario/accidentes/accidentes_2006_2012.dbf \
    inventario.accidentes > datos/inventario/accidentes.sql
shp2pgsql -d -W iso-8859-1 \
    ${viasobras_data_orig}/inventario/aforos/aforos.dbf \
    inventario.aforos > datos/inventario/aforos.sql
shp2pgsql -d -W iso-8859-1 \
    ${viasobras_data_orig}/inventario/inventario/Inventario.dbf \
    inventario.inventario > datos/inventario/inventario.sql
shp2pgsql -d -g the_geom -W iso-8859-1 -s 23029 -I \
    ${viasobras_data_orig}/inventario/rede_carreteras/Vias_Provinciales_datos \
    inventario.rede_carreteras > datos/inventario/rede_carreteras.sql
